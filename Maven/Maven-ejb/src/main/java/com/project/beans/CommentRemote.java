/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.beans;

import com.project.entities.Comment;
import com.project.entities.Event;
import com.project.entities.User;
import javax.ejb.Remote;

/**
 *
 * @author przemic
 */
public interface CommentRemote {
    
    public void addComment(String description_text, Event Event_id, User User_id);
    public Comment getComment(Integer id);
//    public Club getClub(int id) throws NotExistingData;
//    
//    public ClubStats getStats(int id) throws NotExistingData;
//    
//    /*
//     * Tworzy nowy club. Jezeli poda sie miasto ktorego nie ma rzuca wyjatek
//     */
//    public void addClub(String name, String desc, String street, int cityId, String homeNumber) throws NotExistingData, AlreadyExistingAddressException;
//    
//    public Collection<Club> getClubsFromCity(int cityId);
//    /*
//     * Zwraca liste clubow w danym miescie, sortujac wzgledem category. Wartosc category jest wyliczana piorytetowo na podstawie dailyVotes, averageVotes i 
//     * generalVotes. Parametry sa zapisywane, aby potem moc proponowac uzytkownikowi kluby na podstawie jego wyszukan
//     */
//    public List<ClubStats> getClubsFromCityByCategory(int cityId, String categoryName, int userId);
//    
//    /*
//     * Tworzy DailyVotes dla kazdej properties albo oblicza nowa srednia, jezeli juz taki DailyVotes istnieje. Nie pozwala uzytkownikowi zaglosowac
//     * na ten sam klub czesciej niz raz na 30 minut.
//     */
//    public String voteOnClub(int clubId, String properties, int userId) throws JMSException, NotEnoughTimePassedException;
//    
//    /*
//     * Dodaje nowy komentarz. Nie pozwala zaglosowac temu samemu uzytkownikowi czesciej niz raz na minute.
//     */
//    public String commentClub(long clubId, String desc, long userId) throws NotExistingData, NotEnoughTimePassedException;
}
