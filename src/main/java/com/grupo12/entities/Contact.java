package com.grupo12.entities;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="contact")
@Getter
@Setter
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContact;

    @Column(name="street")
    private String street;
    
    @Column(name="number")
    private String number;
    
    @Column(name="email")
    private String email;
    
    @Column(name="phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "locality_id")
    private Locality locality;

	public Contact(Integer idContact, String street, String number, String email, String phone, Locality locality) {
		super();
		this.idContact = idContact;
		this.street = street;
		this.number = number;
		this.email = email;
		this.phone = phone;
		this.locality = locality;
	}

    
}
