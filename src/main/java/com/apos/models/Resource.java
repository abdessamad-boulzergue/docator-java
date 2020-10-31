package com.apos.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name="resource")
public class Resource {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	private String name;
	
	private String description;
	
	@OneToOne
	@JoinColumn(name="max_version_id",referencedColumnName = "id")
	private ResourceVersion maxVersion;
	
	@OneToMany(mappedBy = "resource",fetch = FetchType.EAGER)
	private List<ResourceVersion> versions;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="type_id")
	private ResourceType type ;
	
	public Long getId() {
		return id;
	}



	public String getName() {
		return name;
	}



	public String getDescription() {
		return description;
	}



	public List<ResourceVersion> getVersions() {
		return versions;
	}



	public void setVersions(List versions) {
		this.versions = versions;
	}

	public ResourceType getType() {
		return type;
	}


	public void setType(ResourceType type) {
		this.type = type;
	}

	public ResourceVersion getMaxVersion() {
		return maxVersion;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMaxVersion(ResourceVersion maxVersion) {
		this.maxVersion = maxVersion;
	}



	public static Resource from(JSONObject attributes) {
		Long id = Long.parseLong(attributes.get("resdescid").toString());
		String name = attributes.getString("name");
		Resource res = new Resource();
		res.setName(name);
		res.setId(id);
		return res;
	}

	public static ResourceType getResourceType(String type) {
		
			ResourceType restype =  new ResourceType();
			restype.setName(type);
			return restype;
		
		
	}

	private void setId(Long id) {
	   this.id = id;
	}



	public static Resource getWorkflowResource() {
		Resource resource = new Resource();
		resource.setName("New Workflow");
		resource.setDescription("workflow");
		resource.setType(Resource.getResourceType(ResourceType.TYPE_WORKFLOW));
		return resource;
	}



	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		obj.put("resdescid", this.id);
		obj.put("name", this.name);
		obj.put("version", this.maxVersion.getName());
		return obj;
	}
	
}
