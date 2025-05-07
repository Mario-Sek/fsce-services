package mk.ukim.finki.wp.fcseservices.service;

import mk.ukim.finki.wp.fcseservices.model.base.Room;
import mk.ukim.finki.wp.fcseservices.model.base.RoomType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface RoomService {
    Page<Room> findAllWithPagination(Integer pageNum, Integer pageSize);
    List<Room> findAll();
    Page<Room> findAllWithPaginationFiltered(Integer pageNum, Integer results, String nameSearch, String locationDescriptionSearch,String equipmentDescriptionSearch,Long participantsSearch,RoomType typeSearch);
    Room findByName(String name);
    Room create(String name, String locationDescription, String equipmentDescription, RoomType type, Long capacity);
    Room update(String name,String newName, String locationDescription, String equipmentDescription, RoomType type, Long capacity);
    Room delete(String name);
    List<Room> importData(List<Room> students);
    String toTsv(List<Room> groups);


    Set<Room> findAllByNameIn(Set<String> roomNames);

    Integer calculateTotalCapacityOfRooms(List<Room> list);

    List<Room> findAllByRoomType(RoomType type);
}
