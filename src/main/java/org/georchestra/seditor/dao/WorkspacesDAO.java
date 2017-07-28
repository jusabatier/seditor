package org.georchestra.seditor.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.StringEscapeUtils;
import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.bean.WorkspaceLayer;
import org.georchestra.seditor.bean.WorkspacePermission;
import org.georchestra.seditor.configuration.SEditorPlaceHolder;
import org.georchestra.seditor.permissions.PermissionsHandler;
import org.json.JSONObject;

@Repository
public class WorkspacesDAO implements IWorkspacesDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Resource(name="dataSource")
	protected DataSource dataSource;
	
	public List<Workspace> getWorkspacesList() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Workspace> criteriaQuery = criteriaBuilder.createQuery(Workspace.class);
		Root<Workspace> workspaceRoot = criteriaQuery.from(Workspace.class);
		criteriaQuery.select(workspaceRoot);
		
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			Root<WorkspacePermission> workspacePermissionRoot = criteriaQuery.from(WorkspacePermission.class);
			Expression<String> roleAttr = workspacePermissionRoot.get("role");
			
			if( PermissionsHandler.getInstance().roleList == null ) {
				criteriaQuery.where(criteriaBuilder.equal(roleAttr, "ALL"));
			} else {
				List<String> roleList = new ArrayList<String>();
				for (String role : PermissionsHandler.getInstance().roleList) {
					roleList.add(role.substring(5));
				}
			
				Predicate or = criteriaBuilder.or(roleAttr.in(roleList),criteriaBuilder.equal(roleAttr, ""));
				criteriaQuery.where(or);
			}
		}
	
		TypedQuery<Workspace> lTypedQuery = entityManager.createQuery(criteriaQuery);
		
		return lTypedQuery.getResultList();
	}

	public Workspace createWorkspace(Workspace pWorkspace) {
		entityManager.persist(pWorkspace);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("CREATE TABLE \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(pWorkspace.getTableName());
		queryBuilder.append("\" (id serial NOT NULL, geom geometry(");
		queryBuilder.append(pWorkspace.getFeatureType());
		queryBuilder.append(",");
		queryBuilder.append(SEditorPlaceHolder.getProperty("srsCode"));
		queryBuilder.append("), author text, CONSTRAINT \"");
		queryBuilder.append(pWorkspace.getTableName());
		queryBuilder.append("_pkey\" PRIMARY KEY (id)); GRANT SELECT ON TABLE \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(pWorkspace.getTableName());
		queryBuilder.append("\" TO georchestra;");
		jdbcTemplate.execute(queryBuilder.toString());
		
		return pWorkspace;
	}

	public void deleteWorkspace(Workspace pWorkspace) {
		Workspace lWorkspace = entityManager.getReference(Workspace.class, pWorkspace.getId());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("DROP TABLE IF EXISTS \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(lWorkspace.getTableName());
		queryBuilder.append("\" CASCADE");
		
		jdbcTemplate.execute(queryBuilder.toString());
		
		entityManager.remove(lWorkspace);
	}

	public Workspace getWorkspaceById(Workspace pWorkspace) {
		return entityManager.find(Workspace.class, pWorkspace.getId());
	}

	public Workspace updateWorkspace(Workspace update) {
		Workspace base = getWorkspaceById(update);
		if( !base.getTableName().equals(update.getTableName()) ) {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("ALTER TABLE \"");
			queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
			queryBuilder.append("\".\"");
			queryBuilder.append(base.getTableName());
			queryBuilder.append("\" RENAME TO \"");
			queryBuilder.append(update.getTableName());
			queryBuilder.append("\"");
			jdbcTemplate.execute(queryBuilder.toString());
		}
		
		Workspace lWorkspace = entityManager.getReference(Workspace.class, update.getId());
		lWorkspace.setKey(update.getKey());
		lWorkspace.setWfsUrl(update.getWfsUrl());
		lWorkspace.setWfsTypeName(update.getWfsTypeName());
		lWorkspace.setTableName(update.getTableName());
		lWorkspace.setFeatureType(update.getFeatureType());
		
		entityManager.persist(lWorkspace);
		return update;
	}
	
	public WorkspaceAttribute getAttributeById(WorkspaceAttribute wsa) {
		return entityManager.find(WorkspaceAttribute.class, wsa.getId());
	}
	
	private Integer getNextWorkspaceAttributePosition(Workspace ws) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<WorkspaceAttribute> criteriaQuery = criteriaBuilder.createQuery(WorkspaceAttribute.class);
		Root<WorkspaceAttribute> workspaceAttributeRoot = criteriaQuery.from(WorkspaceAttribute.class);
		criteriaQuery.select(workspaceAttributeRoot);
		Expression<Integer> wsAttr = workspaceAttributeRoot.get("workspace");
		Expression<Integer> positionAttr = workspaceAttributeRoot.get("position");
		criteriaQuery.where(criteriaBuilder.equal(wsAttr, ws.getId()));
		criteriaQuery.orderBy(criteriaBuilder.desc(positionAttr));
		TypedQuery<WorkspaceAttribute> lTypedQuery = entityManager.createQuery(criteriaQuery);
		
		List<WorkspaceAttribute> lsResult = lTypedQuery.setMaxResults(1).getResultList();
		
		if( lsResult.isEmpty() ) return 1;
		
		return lsResult.get(0).getPosition()+1;
	}

	public WorkspaceAttribute createWorkspaceAttribute(WorkspaceAttribute wsa) {
		wsa.setPosition(getNextWorkspaceAttributePosition(wsa.getWorkspace()));
		entityManager.persist(wsa);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("ALTER TABLE \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(wsa.getWorkspace().getTableName());
		queryBuilder.append("\" ADD \"");
		queryBuilder.append(wsa.getName());
		queryBuilder.append("\" text");
		jdbcTemplate.execute(queryBuilder.toString());
		
		return wsa;
	}

	public void deleteWorkspaceAttribute(WorkspaceAttribute wsa) {
		WorkspaceAttribute reference = entityManager.getReference(WorkspaceAttribute.class, wsa.getId());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("ALTER TABLE \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(reference.getWorkspace().getTableName());
		queryBuilder.append("\" DROP \"");
		queryBuilder.append(reference.getName());
		queryBuilder.append("\"");
		jdbcTemplate.execute(queryBuilder.toString());
		
		entityManager.remove(reference);
	}

	public WorkspaceAttribute updateWorkspaceAttribute(WorkspaceAttribute wsa) {
		WorkspaceAttribute base = entityManager.find(WorkspaceAttribute.class, wsa.getId());
		
		if( !base.getName().equals(wsa.getName()) ) {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("ALTER TABLE \"");
			queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
			queryBuilder.append("\".\"");
			queryBuilder.append(base.getWorkspace().getTableName());
			queryBuilder.append("\" RENAME COLUMN \"");
			queryBuilder.append(base.getName());
			queryBuilder.append("\" TO \"");
			queryBuilder.append(wsa.getName());
			queryBuilder.append("\"");
			jdbcTemplate.execute(queryBuilder.toString());
		}
		
		WorkspaceAttribute update = entityManager.getReference(WorkspaceAttribute.class, wsa.getId());
		update.setWorkspace(wsa.getWorkspace());
		update.setLabel(wsa.getLabel());
		update.setName(wsa.getName());
		update.setType(wsa.getType());
		update.setValues(wsa.getValues());
		update.setRequired(wsa.getRequired());
		
		entityManager.persist(update);
		return update;
	}
	
	private Integer getNextWorkspaceLayerPosition(Workspace ws) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<WorkspaceLayer> criteriaQuery = criteriaBuilder.createQuery(WorkspaceLayer.class);
		Root<WorkspaceLayer> workspaceLayerRoot = criteriaQuery.from(WorkspaceLayer.class);
		criteriaQuery.select(workspaceLayerRoot);
		Expression<Integer> wsAttr = workspaceLayerRoot.get("workspace");
		Expression<Integer> positionAttr = workspaceLayerRoot.get("position");
		criteriaQuery.where(criteriaBuilder.equal(wsAttr, ws.getId()));
		criteriaQuery.orderBy(criteriaBuilder.desc(positionAttr));
		TypedQuery<WorkspaceLayer> lTypedQuery = entityManager.createQuery(criteriaQuery);
		
		List<WorkspaceLayer> lsResult = lTypedQuery.setMaxResults(1).getResultList();
		
		if( lsResult.isEmpty() ) return 1;
		
		return lsResult.get(0).getPosition()+1;
	}

	public WorkspaceLayer createWorkspaceLayer(WorkspaceLayer wsl) {
		wsl.setPosition(getNextWorkspaceLayerPosition(wsl.getWorkspace()));
		entityManager.persist(wsl);
		return wsl;
	}

	public WorkspaceLayer getLayerById(WorkspaceLayer wsl) {
		return entityManager.find(WorkspaceLayer.class, wsl.getId());
	}

	public void deleteWorkspaceLayer(WorkspaceLayer wsl) {
		WorkspaceLayer toDelete = entityManager.getReference(WorkspaceLayer.class, wsl.getId());
		entityManager.remove(toDelete);
	}

	public WorkspaceLayer updateWorkspaceLayer(WorkspaceLayer wsl) {
		WorkspaceLayer update = entityManager.getReference(WorkspaceLayer.class, wsl.getId());
		update.setWorkspace(wsl.getWorkspace());
		update.setUrl(wsl.getUrl());
		update.setSnappable(wsl.getSnappable());
		update.setTypeName(wsl.getTypeName());
		update.setVisible(wsl.getVisible());
		
		entityManager.persist(update);
		return update;
	}

	public WorkspacePermission createWorkspacePermission(WorkspacePermission wsp) {
		entityManager.persist(wsp);
		return wsp;
	}

	public WorkspacePermission getPermissionById(WorkspacePermission wsp) {
		return entityManager.find(WorkspacePermission.class, wsp.getId());
	}

	public void deleteWorkspacePermission(WorkspacePermission wsp) {
		WorkspacePermission toDelete = entityManager.getReference(WorkspacePermission.class, wsp.getId());
		entityManager.remove(toDelete);
	}

	public WorkspacePermission updateWorkspacePermission(WorkspacePermission wsp) {
		WorkspacePermission update = entityManager.getReference(WorkspacePermission.class, wsp.getId());
		update.setWorkspace(wsp.getWorkspace());
		update.setRole(wsp.getRole());
		update.setAccess(wsp.getAccess());
		
		entityManager.persist(update);
		return update;
	}

	public Workspace getWorkspaceByKey(Workspace lWorkspace) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Workspace> criteriaQuery = criteriaBuilder.createQuery(Workspace.class);
		Root<Workspace> workspaceRoot = criteriaQuery.from(Workspace.class);
		criteriaQuery.select(workspaceRoot);
		
		Expression<String> keyAttr = workspaceRoot.get("key");
		criteriaQuery.where(criteriaBuilder.equal(keyAttr, lWorkspace.getKey()));
		
		TypedQuery<Workspace> lTypedQuery = entityManager.createQuery(criteriaQuery);
		return lTypedQuery.getSingleResult();
	}

	public void positionUpWorkspaceLayer(WorkspaceLayer wsl) {
		WorkspaceLayer toUpdate = entityManager.getReference(WorkspaceLayer.class, wsl.getId());
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<WorkspaceLayer> criteriaQuery = criteriaBuilder.createQuery(WorkspaceLayer.class);
		Root<WorkspaceLayer> workspaceLayerRoot = criteriaQuery.from(WorkspaceLayer.class);
		criteriaQuery.select(workspaceLayerRoot);
		Expression<Integer> wsAttr = workspaceLayerRoot.get("workspace");
		Expression<Integer> positionAttr = workspaceLayerRoot.get("position");
		criteriaQuery.where(criteriaBuilder.equal(wsAttr, toUpdate.getId()));
		criteriaQuery.where(criteriaBuilder.lessThan(positionAttr, toUpdate.getPosition()));
		criteriaQuery.orderBy(criteriaBuilder.desc(positionAttr));
		TypedQuery<WorkspaceLayer> lTypedQuery = entityManager.createQuery(criteriaQuery);
		
		WorkspaceLayer next = lTypedQuery.setMaxResults(1).getSingleResult();
		
		next.setPosition(toUpdate.getPosition());
		toUpdate.setPosition(toUpdate.getPosition()-1);
		
		entityManager.persist(next);
		entityManager.persist(toUpdate);
	}

	public void positionDownWorkspaceLayer(WorkspaceLayer wsl) {
		WorkspaceLayer toUpdate = entityManager.getReference(WorkspaceLayer.class, wsl.getId());
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<WorkspaceLayer> criteriaQuery = criteriaBuilder.createQuery(WorkspaceLayer.class);
		Root<WorkspaceLayer> workspaceLayerRoot = criteriaQuery.from(WorkspaceLayer.class);
		criteriaQuery.select(workspaceLayerRoot);
		Expression<Integer> wsAttr = workspaceLayerRoot.get("workspace");
		Expression<Integer> positionAttr = workspaceLayerRoot.get("position");
		criteriaQuery.where(criteriaBuilder.equal(wsAttr, toUpdate.getId()));
		criteriaQuery.where(criteriaBuilder.greaterThan(positionAttr, toUpdate.getPosition()));
		criteriaQuery.orderBy(criteriaBuilder.asc(positionAttr));
		TypedQuery<WorkspaceLayer> lTypedQuery = entityManager.createQuery(criteriaQuery);
		WorkspaceLayer next = lTypedQuery.setMaxResults(1).getSingleResult();
		
		next.setPosition(toUpdate.getPosition());
		toUpdate.setPosition(toUpdate.getPosition()+1);
		
		entityManager.persist(next);
		entityManager.persist(toUpdate);
	}
	
	public void positionUpWorkspaceAttribute(WorkspaceAttribute wsa) {
		WorkspaceAttribute toUpdate = entityManager.getReference(WorkspaceAttribute.class, wsa.getId());
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<WorkspaceAttribute> criteriaQuery = criteriaBuilder.createQuery(WorkspaceAttribute.class);
		Root<WorkspaceAttribute> workspaceAttributeRoot = criteriaQuery.from(WorkspaceAttribute.class);
		criteriaQuery.select(workspaceAttributeRoot);
		Expression<Integer> wsAttr = workspaceAttributeRoot.get("workspace");
		Expression<Integer> positionAttr = workspaceAttributeRoot.get("position");
		criteriaQuery.where(criteriaBuilder.equal(wsAttr, toUpdate.getId()));
		criteriaQuery.where(criteriaBuilder.lessThan(positionAttr, toUpdate.getPosition()));
		criteriaQuery.orderBy(criteriaBuilder.desc(positionAttr));
		TypedQuery<WorkspaceAttribute> lTypedQuery = entityManager.createQuery(criteriaQuery);
		
		WorkspaceAttribute next = lTypedQuery.setMaxResults(1).getSingleResult();
		
		next.setPosition(toUpdate.getPosition());
		toUpdate.setPosition(toUpdate.getPosition()-1);
		
		entityManager.persist(next);
		entityManager.persist(toUpdate);
	}

	public void positionDownWorkspaceAttribute(WorkspaceAttribute wsa) {
		WorkspaceAttribute toUpdate = entityManager.getReference(WorkspaceAttribute.class, wsa.getId());
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<WorkspaceAttribute> criteriaQuery = criteriaBuilder.createQuery(WorkspaceAttribute.class);
		Root<WorkspaceAttribute> workspaceAttributeRoot = criteriaQuery.from(WorkspaceAttribute.class);
		criteriaQuery.select(workspaceAttributeRoot);
		Expression<Integer> wsAttr = workspaceAttributeRoot.get("workspace");
		Expression<Integer> positionAttr = workspaceAttributeRoot.get("position");
		criteriaQuery.where(criteriaBuilder.equal(wsAttr, toUpdate.getId()));
		criteriaQuery.where(criteriaBuilder.greaterThan(positionAttr, toUpdate.getPosition()));
		criteriaQuery.orderBy(criteriaBuilder.asc(positionAttr));
		TypedQuery<WorkspaceAttribute> lTypedQuery = entityManager.createQuery(criteriaQuery);
		WorkspaceAttribute next = lTypedQuery.setMaxResults(1).getSingleResult();
		
		next.setPosition(toUpdate.getPosition());
		toUpdate.setPosition(toUpdate.getPosition()+1);
		
		entityManager.persist(next);
		entityManager.persist(toUpdate);
	}

	public List<String> getJSONFeaturesByWorkspaceKey(Workspace ws) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT row_to_json(f) AS feature ");
		queryBuilder.append("FROM (SELECT 'Feature' AS type, ST_AsGeoJSON(geom)::json AS geometry, row_to_json((Select l FROM ( SELECT id AS feat_id, author, ");
		for( Iterator<WorkspaceAttribute> i = ws.getAttributes().iterator(); i.hasNext();) {
			WorkspaceAttribute wsa = i.next();
			queryBuilder.append("\"");
			queryBuilder.append(wsa.getName());
			queryBuilder.append("\"");
			if( i.hasNext() ) queryBuilder.append(", ");
		}
		queryBuilder.append(") AS l )) AS properties FROM \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(ws.getTableName());
		queryBuilder.append("\" AS l ) AS f");
		
		
		return jdbcTemplate.queryForList(queryBuilder.toString(),String.class);
	}
	
	public String getFeatureAuthorByIdAndWorkspace(int fid, Workspace ws) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT author FROM \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(ws.getTableName());
		queryBuilder.append("\" WHERE id = ");
		queryBuilder.append(fid);
		
		List<Map<String,Object>> result = jdbcTemplate.queryForList(queryBuilder.toString());
		if( !result.isEmpty() ) {
			return result.get(0).get("author").toString();
		}
		return null;
	}

	@Override
	public JSONObject addFeatureToWorkspace(Workspace workspace, JSONObject properties, JSONObject geometry, String author) {
		JSONObject reponse = new JSONObject();
		
		List<String> parameters = new ArrayList<String>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("INSERT INTO \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(workspace.getTableName());
		queryBuilder.append("\" (");
		
		for( WorkspaceAttribute wsa : workspace.getAttributes() ) {
			queryBuilder.append(wsa.getName());
			queryBuilder.append(", ");
		}
		
		queryBuilder.append("geom, author) VALUES (");
		
		for( WorkspaceAttribute wsa : workspace.getAttributes() ) {
			queryBuilder.append("?, ");
			parameters.add(StringEscapeUtils.escapeHtml4(properties.getString(wsa.getName())));
		}
		
		queryBuilder.append("ST_SetSRID(ST_GeomFromGeoJSON('");
		queryBuilder.append(geometry.toString());
		queryBuilder.append("'),");
		queryBuilder.append(SEditorPlaceHolder.getProperty("srsCode"));
		queryBuilder.append("), '");
		queryBuilder.append(author);
		queryBuilder.append("')");
		try {
			int result = jdbcTemplate.update(queryBuilder.toString(), parameters.toArray());
			if( result > 0 ) {
				reponse.put("statut","success");
			} else {
				reponse.put("statut","error");
				reponse.put("message","No row affected.");
			}
		} catch(DataAccessException exception) {
			reponse.put("statut","error");
			reponse.put("message",exception.getMessage());
		}
		return reponse;
	}

	@Override
	public JSONObject updateWorkspaceFeature(Workspace ws, JSONObject properties, JSONObject geometry, String author) {
		JSONObject reponse = new JSONObject();
		
		List<String> parameters = new ArrayList<String>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("UPDATE \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(ws.getTableName());
		queryBuilder.append("\" SET ");
		for( WorkspaceAttribute wsa : ws.getAttributes() ) {
			queryBuilder.append(wsa.getName());
			queryBuilder.append(" = ?, ");
			parameters.add(StringEscapeUtils.escapeHtml4(properties.getString(wsa.getName())));
		}
		queryBuilder.append("author = '");
		queryBuilder.append(author);
		queryBuilder.append("', geom = ST_SetSRID(ST_GeomFromGeoJSON('");
		queryBuilder.append(geometry.toString());
		queryBuilder.append("'),");
		queryBuilder.append(SEditorPlaceHolder.getProperty("srsCode"));
		queryBuilder.append(") WHERE id = ?::int");
		parameters.add(String.valueOf(properties.getInt("feat_id")));
		
		try {
			int result = jdbcTemplate.update(queryBuilder.toString(), parameters.toArray());
			if( result > 0 ) {
				reponse.put("statut","success");
			} else {
				reponse.put("statut","error");
				reponse.put("message","No row affected.");
			}
		} catch(DataAccessException exception) {
			reponse.put("statut","error");
			reponse.put("message",exception.getMessage());
		}
		return reponse;
	}

	@Override
	public JSONObject deleteWorkspaceFeature(Workspace ws, int fid) {
		JSONObject reponse = new JSONObject();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("DELETE FROM \"");
		queryBuilder.append(SEditorPlaceHolder.getProperty("schema.name"));
		queryBuilder.append("\".\"");
		queryBuilder.append(ws.getTableName());
		queryBuilder.append("\" WHERE id = ");
		queryBuilder.append(fid);
		
		try {
			int result = jdbcTemplate.update(queryBuilder.toString());
			if( result > 0 ) {
				reponse.put("statut","success");
			} else {
				reponse.put("statut","error");
				reponse.put("message","No row affected.");
			}
		} catch(DataAccessException exception) {
			reponse.put("statut","error");
			reponse.put("message",exception.getMessage());
		}
		return reponse;
	}
}
