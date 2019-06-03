package com.razboi.razboi.networking.utils;


import com.razboi.razboi.business.service.user.dto.UserDTO;

import java.rmi.RemoteException;

public interface IObserver {

    void userLoggedIn(UserDTO user) throws RemoteException, ServerException;
    // void userEnteredGame(UserDTO user) throws RemoteException, ServerException;
//    void carteAleasa(Iterable<Joc> participants) throws ServerException;
//    void jocPornit(Iterable<Joc> participants) throws ServerException;
}
