package com.coredumpproject.coredump.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

@Entity
@Cacheable(false)
@XmlRootElement
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String name;

	/*
	@ManyToOne
	//@JsonBackReference
	private Item parent;
	*/

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Action> actions = new HashSet<Action>();

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", orphanRemoval = true)
	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.ALL)
	//@JsonManagedReference
	// works without parent.
	@OneToMany(fetch = FetchType.EAGER)
	private Set<Item> items = new HashSet<Item>();


	@Column
	@Lob
	Hashtable<String, Integer> attributes = new Hashtable<String,Integer>();

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Item)) {
			return false;
		}
		Item other = (Item) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (name != null && !name.trim().isEmpty())
			result += "name: " + name;
		return result;
	}

	/*
	public Item getParent() {
		return this.parent;
	}

	public void setParent(final Item parent) {
		this.parent = parent;
	}
	*/


	public Set<Action> getActions() {
		return this.actions;
	}

	public void setActions(final Set<Action> actions) {
		this.actions = actions;
	}

	public Set<Item> getItems() {
		return this.items;
	}

	public void setItems(final Set<Item> items) {
		this.items = items;
		/*
		for (Item i:this.items){
			i.setParent(this);
		}
		*/
	}

	public Hashtable<String, Integer> getAttributes() {
		return attributes;
	}

	public void setAttributes(Hashtable<String,Integer> attributes) {
		this.attributes = attributes;
	}
}