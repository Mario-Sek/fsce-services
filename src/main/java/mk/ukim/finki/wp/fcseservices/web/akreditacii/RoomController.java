package mk.ukim.finki.wp.fcseservices.web.akreditacii;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.fcseservices.model.dto.RoomDto;
import mk.ukim.finki.wp.fcseservices.model.room.Room;
import mk.ukim.finki.wp.fcseservices.model.room.RoomType;
import mk.ukim.finki.wp.fcseservices.repository.ImportRepository;
import mk.ukim.finki.wp.fcseservices.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
public class RoomController {
    private final RoomService roomService;
    private final ImportRepository importRepository;


    public RoomController(RoomService roomService, ImportRepository importRepository) {
        this.roomService = roomService;
        this.importRepository = importRepository;
    }

    @GetMapping("/admin/rooms")
    public String findAllRoomsFiltered(Model model,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer results,
                                          @RequestParam(required = false) String nameSearch,
                                          @RequestParam(required = false) String locationDescriptionSearch,
                                          @RequestParam(required = false) String equipmentDescriptionSearch,
                                          @RequestParam(required = false) Long participantsSearch,
                                          @RequestParam(required = false) RoomType typeSearch

    ){
        Page<Room> roomPage;

        if (nameSearch == null && locationDescriptionSearch == null && equipmentDescriptionSearch==null && participantsSearch == null && typeSearch == null)  {
            roomPage = roomService.findAllWithPagination(pageNum,results);

        } else {
            roomPage =  roomService.findAllWithPaginationFiltered(pageNum,results,nameSearch,locationDescriptionSearch,equipmentDescriptionSearch,participantsSearch,typeSearch);
            model.addAttribute("nameSearch", nameSearch);
            model.addAttribute("locationDescriptionSearch", locationDescriptionSearch);
            model.addAttribute("participantsSearch", participantsSearch);
            model.addAttribute("typeSearch",typeSearch);

        }
        model.addAttribute("types",RoomType.values());
        model.addAttribute("rooms", roomPage);
        return "akreditacii/room/room";
    }

    @PostMapping("/admin/rooms/delete/{name}")
    public String deleteRoom(@PathVariable String name){
        this.roomService.delete(name);
        return "redirect:/admin/rooms";

    }
    @GetMapping("/admin/rooms/edit/{name}")
    public String editRoomPage(@PathVariable String name,Model model) {
        model.addAttribute("room", roomService.findByName(name));
        model.addAttribute("types",RoomType.values());
        return "akreditacii/room/edit_room";
    }
    @PostMapping("/admin/rooms/edit/{name}")
    public String editRoom(
            @PathVariable String name,
            @RequestParam String newName,
            @RequestParam String locationDescription,
            @RequestParam String equipmentDescription,
            @RequestParam RoomType type,
            @RequestParam Long capacity) {
        this.roomService.update(name,newName,locationDescription,equipmentDescription,type,capacity);
        return "redirect:/admin/rooms";
    }
    @GetMapping("/admin/rooms/add")
    public String addRoomPage(Model model) {
        Room room = new Room();
        model.addAttribute("types",RoomType.values());
        model.addAttribute("room", room);
        return "akreditacii/room/add_room";
    }
    @PostMapping("/admin/rooms/add")
    public String saveRoom(
            @RequestParam String name,
            @RequestParam String locationDescription,
            @RequestParam String equipmentDescription,
            @RequestParam RoomType type,
            @RequestParam Long capacity) {
        this.roomService.create(name,locationDescription,equipmentDescription,type,capacity);
        return "redirect:/admin/rooms";
    }



    @GetMapping("/admin/rooms/export")
    public void export(@RequestParam(required = false) String nameSearch,
                       @RequestParam(required = false) String locationDescriptionSearch,
                       @RequestParam(required = false) String equipmentDescriptionSearch,
                       @RequestParam(required = false) Long participantsSearch,
                       @RequestParam(required = false) RoomType typeSearch,HttpServletResponse response) {
        String tsv = roomService.toTsv(roomService.findAllWithPaginationFiltered( 1,100000,nameSearch, locationDescriptionSearch, equipmentDescriptionSearch,participantsSearch,typeSearch).getContent());

        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"rooms.tsv\"");

        try (BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            outputStream.write(tsv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/admin/rooms/sample-tsv")
    public void sampleTsv(HttpServletResponse response) {
        List<RoomDto> example = new ArrayList<>();
        example.add(new RoomDto("Амф ТМФ", "", "", "CLASSROOM","150"));

        doExport(response, example);
    }


    public void doExport(HttpServletResponse response, List<RoomDto> data) {
        String fileName = "rooms_example.tsv";
        response.setContentType("text/tab-separated-values");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            importRepository.writeToOutputStream(RoomDto.class, data, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}