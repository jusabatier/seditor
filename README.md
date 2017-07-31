# sEditor
Webapp to put behind Georchestra SP, allow to manage and edit layer's features and their attributes.

## Requirements

* Postgres 9.4 / PostGIS 2.1
* Java 1.7 or higher
* Tomcat 7 or higher

## Description

This application is composed of two sides : 

* An editor viewer, accessible at `/` : 
   It load a workspace configuration and allow to add / modify and delete features and their attributes related to your privileges.
   
   Actually available : 
   * create (point, line, polygon, circle)(with attributes)
   * modify (with attributes)
   * translate
   * select
   * delete
   * snapping
   * layerlist
   * navigation (with getfeatureinfo)
   
* An administration panel, accessible at `/workspaces` :
   It allow administrators to manage workspaces : 
   * Define geometry type, workspace key, database workspace table
   * Manage features required attributes (types, names and values)
   * Manage layers to include (WMS for viewing and WFS for snapping)
   * Manage privilèges (related to LDAP roles)

## To do

* Front-end : 
   * Implement internationalization (actually hardcoded in french)
   * Add descriptions when hover buttons
   * Add create regular polygons
   
* Backend : 
   * Manage workspaces buttons related to user role (hide New ws, Edit Delete if not allowed)
   * Implement a download layer function when user have access to workspace
   * Add more input types (select, tinyMCE, file, ...)

Create an issue if you have some suggestions.

## Installation

### Database

1. Create a role georchestra if not exists (for grouping all georchestra's app roles)
```sql
CREATE ROLE georchestra
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
```

2. Create a role for seditor app
```sql
CREATE ROLE seditor LOGIN
  WITH PASSWORD '<password>'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
GRANT georchestra TO seditor;
```

3. Run the [seditor/scripts/seditor.sql](https://github.com/jusabatier/seditor/blob/master/scripts/seditor.sql) SQL script to create seditor schema and tables

4. Then you will have to add the JDBC resources to the tomcat where you deploy the sEditor app : 
In conf/context.xml add in Context : 
```xml
<Resource name="jdbc/seditor" auth="Container" type="javax.sql.DataSource"
		driverClassName="org.postgresql.Driver"
		url="jdbc:postgresql://<server_ip>:5432/<db_name>"
		username="seditor" password="<password>"
		maxTotal="15"
		initialSize="0"
		minIdle="0"
		maxIdle="6"
		maxWaitMillis="10000"
		timeBetweenEvictionRunsMillis="30000"
		minEvictableIdleTimeMillis="60000"
		testWhileIdle="true"
		validationQuery="SELECT 1"/>
  <ResourceLink name="jdbc/seditor"
		global="jdbc/seditor"
		type="javax.sql.DataSource"/>
```
Be sure to replace `<server_ip>`, `<db_name>` and `<password>` with appropriated values.

### Build the war

If not already done, install maven : `apt-get install maven`.

And just run `mvn clean install` at the root of the project.

You'll find the war in the target directory.

### Deploy the application

sEditor have to be placed behind georchestra's security proxy, as the privilèges management is based on georchestra's roles and username (sec-roles & sec-username request headers).

But you can simulate those requests headers with a Firefox addon like "Modify Headers".

It also use the same datadir logic for properties files.
So you'll have to put a properties file under the seditor directory of your georchestra datadir.

Here is the file you can copy, or modify before build : https://github.com/jusabatier/seditor/blob/master/src/main/resources/seditor.properties


* For deploy the app, just put the WAR in the tomcat webapp folder, wait for it to startup.

Once done, you can access the admin on : 

`http://<tomcat_ip>:<tomcat_port>/seditor/workspaces`

If you have an admin role, like set in the seditor.properties file.

At this step, you can access directly to the application, but you have to set request headers via a plugin.

* For deploy it behind Georchestra SP, you have to : 

1. Add the seditor app to Reverse Proxy redirections, for example on nginx in the `/etc/nginx/sites-available/default` file : 
```
location ~ ^/(seditor|analytics|<some_other_apps>)(/?).*$ {
  proxy_set_header Host $host;
	proxy_set_header X-Real-IP $remote_addr;
	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
	proxy_max_temp_file_size 0;
	client_max_body_size 500M;
	client_body_buffer_size 128k;
	proxy_connect_timeout 90;
	proxy_send_timeout 90;
	proxy_read_timeout 90;
	proxy_buffer_size 4k;
	proxy_buffers 4 32k;
	proxy_busy_buffers_size 64k;
  proxy_temp_file_write_size 64k;
  
  proxy_redirect off;
  proxy_pass http://<security_proxy_ip>:8080$request_uri;
}
```
And restart nginx for load new config.

2. Add the seditor app to Security Proxy targets, in SP datadir add to `targets-mapping.properties` : 
```
seditor=http://<tomcat_server_ip>:<tomcat_server_port>/seditor/
```
And restart the SP's tomcat.
