package com.razboi.razboi.networking.utils;


import com.razboi.razboi.business.service.user.dto.UserDTO;
import com.razboi.razboi.networking.Response;
import com.razboi.razboi.persistence.user.entity.User;

public interface IServer {
    Response login(User user, IObserver client) throws ServerException;

    Response logout(UserDTO user, IObserver client) throws ServerException;

    Response getAllLoggedInUsers() throws ServerException;
//    void startJoc() throws JocException;

//    String getAspectByUsername(String username) throws JocException;
//    Iterable<Participant> getParticipanti() throws JocException;
//    void addNota(Participant participant) throws JocException;

//    void alegeCarte(Joc participant) throws ServerException;

}
