package com.razboi.razboi.networking.utils;


import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.persistence.game.entity.Game;
import com.razboi.razboi.persistence.game.entity.Player;

import java.rmi.RemoteException;

public interface IObserver {

    void userLoggedIn(UserDTO user) throws RemoteException, ServerException;
    void gameStarted(Game game)throws RemoteException, ServerException;

    void wordChosen(Player player) throws RemoteException, ServerException;
}
