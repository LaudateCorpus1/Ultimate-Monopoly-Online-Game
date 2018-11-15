package com.nullPointer.Model.Square;
import com.nullPointer.Model.GameEngine;
import com.nullPointer.Model.Player;

public class UtilitySquare extends Square {

	private Player owner;
	private int price;
	
	public UtilitySquare(String n, String t) {
		super(n, t);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void evaluateSquare(GameEngine gameEngine) {
		if(this.getOwner() == null) {
			gameEngine.publishEvent("buy");
		} else {
			int multiplier = 0;
			switch(GameEngine.getOwnedUtilities()) {
			case 1:
				multiplier = 4;
			case 2:
				multiplier = 10;
			case 3:
				multiplier = 20;
			case 4:
				multiplier = 40;
			case 5:
				multiplier = 80;
			case 6:
				multiplier = 100;
			case 7:
				multiplier = 120;
			case 8:
				multiplier = 150;
			}
			int diceVal = gameEngine.getRegularDie().getLastValues().get(0) + gameEngine.getRegularDie().getLastValues().get(1) + gameEngine.getSpeedDie().getLastValues().get(0);
			gameEngine.payRent(gameEngine.getPlayerController().getCurrentPlayer(), diceVal*multiplier);
		}
		
	}
	public int getPrice() {
		return price;
	}
	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}

}
