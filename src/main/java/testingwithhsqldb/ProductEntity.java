/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingwithhsqldb;

/**
 *
 * @author Diego
 */
public class ProductEntity {
    
    private int id;
    private String nom;
    private int prix;
    
    public ProductEntity(int a, String b, int c) {
		this.id = a;
		this.nom = b;
		this.prix = c;
	}

	/**
	 * Get the value of customerId
	 *
	 * @return the value of customerId
	 */
	public int getProductId() {
		return id;
	}

	/**
	 * Get the value of name
	 *
	 * @return the value of name
	 */
	public String getName() {
		return nom;
	}

	/**
	 * Get the value of addressLine1
	 *
	 * @return the value of addressLine1
	 */
	public int getprix() {
		return prix;
	}


}
