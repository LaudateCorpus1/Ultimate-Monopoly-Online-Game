package com.nullPointer.Domain.Model.Cards;

import com.nullPointer.Domain.Controller.MoneyController;
import com.nullPointer.Domain.Controller.PlayerController;
import com.nullPointer.Domain.Model.*;

public class ChanceGoToJail extends ChanceCard {
	private MoneyController moneyController = MoneyController.getInstance();
	private PlayerController playerController = PlayerController.getInstance();


	public ChanceGoToJail(String title, boolean isImmediate) {
		super(title, isImmediate);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void playCard(GameEngine gameEngine) {
		// TODO Auto-generated method stub
		int jail = 66;
		Player currentPlayer = playerController.getCurrentPlayer();
		playerController.putInJail();
		playerController.movePlayer(jail);
		gameEngine.publishEvent("teleport" + jail);
	}

}
