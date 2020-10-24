package com.apos.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


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
	private Long typeId = 1L;
	
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

	public Long getTypeId() {
		return typeId;
	}


	public void setTypeId(Long typeId) {
		this.typeId = typeId;
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
	
}
