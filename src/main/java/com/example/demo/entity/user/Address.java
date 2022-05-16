package com.example.demo.entity.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address implements Cloneable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String address;
	
	@Override
	public int hashCode() {
		return 13;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Address)) return false;
		Address o = (Address) obj;
		if (o.getId() == null || this.getId() == null) return false;
		return this.getId() == o.getId();
	}
	@Override
	public String toString() {
		return "ID: " + this.getId() + " - Address: " + this.getAddress();
	}
	
//	@Override
//	public Address clone() {
//		return new()
//	}
}
