package com.razboi.razboi.networking.utils;


import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.persistence.game.entity.Game;

import java.rmi.RemoteException;

public interface IObserver {

    void userLoggedIn(UserDTO user) throws RemoteException, ServerException;
    void gameStarted(Game game)throws RemoteException, ServerException;
//    void carteAleasa(Iterable<Joc> participants) throws ServerException;
//    void jocPornit(Iterable<Joc> participants) throws ServerException;
}
