package com.nullPointer.Model.Square;

import java.util.ArrayList;

import com.nullPointer.Model.GameEngine;
import com.nullPointer.Model.Player;

public class PropertySquare extends Square {
	private boolean isOwned;
	private Player owner;
	private int price;
	private String color;
	private int rentFactor;
	/*
	 * About rentList & rentListIndex:
	 * The list rentList keeps all the available info about the money on each property (base rent, rent after n houses, mortgage value etc.)
	 * rentListIndex is the index used to access the list.
	 * rentList(0) = base rent
	 * rentList(1) = rent after 1 house
	 * rentList(2) =   "	"	2	"
	 * rentList(3) = 	"	"	3	"
	 * rentList(4) = 	"	"	4	"
	 * rentList(5) = rent with hotel
	 * rentList(6) = rent with skyscraper
	 * rentList(7) = mortgage value
	 * ps: not sure if mortgage value should be kept in that list
	 */
	private int rentListIndex;
	private int[] rentList;
	private boolean isMortgaged;
	public PropertySquare(String n, String t, int p, String color, int[] rentList) {
		super(n, t);
		setOwned(false);
		setOwner(null);
		setPrice(p);
		setRentList(rentList);
		setMortgage(false);
		setRentListIndex(0);
		setRentFactor(1);
		setColor(color);
	}

    public PropertySquare(String n, String t, int p, String color, ArrayList<Integer> rentList) {
        super(n, t);
        setOwner(null);
        setPrice(p);
        setRentList(rentList);
        setMortgage(false);
        setRentListIndex(0);
        setRentFactor(1);
        setColor(color);
    }

	
	
	
	public int getRent() {
		return calculateRent();
	}
	private int calculateRent()
	{
		if (getRentListIndex() <= 6) return rentFactor*rentList[getRentListIndex()];
		else return 0;
	}

    private int calculateRent() {
        if (getRentListIndex() <= 6) return rentFactor * rentList.get(getRentListIndex());
        else return 0;
    }

    public String getColor() {
        return color;
    }

    public int numHouses() {
        if (getRentListIndex() <= 4) return getRentListIndex();
        else return 0;
    }

    public boolean hasHotel() {
        return (getRentListIndex() == 5);
    }

    public boolean hasSkyscraper() {
        return (getRentListIndex() == 6);
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    public boolean getMortgaged() {
        return isMortgaged;
    }

    public void mortgage() {
        if (!isMortgaged) {
            setMortgage(true);
            rentListIndex = 7;
        }
    }

	
	
	
	public int[] getRentList() {
		return rentList;
	}
	public void setRentList(int[] rentList) {
		this.rentList = rentList;
	}

    private void setMortgage(boolean mortgageVal) {
        this.isMortgaged = mortgageVal;
    }


    public boolean canBeImproved() {
        return (getRentListIndex() < 6);
    }

    public boolean canBeDowngraded() {
        return (getRentListIndex() > 0);
    }

    private int getRentListIndex() {
        return rentListIndex;
    }

    private void setRentListIndex(int rentListIndex) {
        this.rentListIndex = rentListIndex;
    }

    public ArrayList<Integer> getRentList() {
        return rentList;
    }

    public void setRentList(ArrayList<Integer> rentList) {
        this.rentList = rentList;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int newPrice) {
        this.price = newPrice;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        if (!isOwned()) this.owner = owner;
    }

    public int getRentFactor() {
        return rentFactor;
    }

    public void setRentFactor(int rentFactor) {
        this.rentFactor = rentFactor;
    }

    public boolean isOwned() {
        return owner != null;
    }

    @Override
    public void evaluateSquare(GameEngine gameEngine) {
        if (this.getOwner() == null) {
            gameEngine.publishEvent("buy");
        } else {
            gameEngine.payRent(gameEngine.getPlayerController().getCurrentPlayer(), this.getOwner(), this.getRent());
            System.out.println("CurrentPlayer paid rent");
            gameEngine.nextTurn();
        }

    }

    @Override
    public String toString() {
        return getName();
    }

}
