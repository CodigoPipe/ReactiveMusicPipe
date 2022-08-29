package ec.com.reactive.music.web;


import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.service.IPlayListService;
import ec.com.reactive.music.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PlayListResource {

    @Autowired
    private ISongService songService;

    @Autowired
    private IPlayListService playListService;

    @GetMapping("/findAllPlaylist")
    private Mono<ResponseEntity<Flux<PlaylistDTO>>> getPlaylist(){
        return playListService.findAllPlayList();
    }

    //GET
    @GetMapping("/findPlaylist/{id}")
    private Mono<ResponseEntity<PlaylistDTO>> getPlaylistById(@PathVariable String id){
        return playListService.findPlayListById(id);
    }

    //POST
    @PostMapping("/savePlaylist")
    private Mono<ResponseEntity<PlaylistDTO>> savePlaylist(@RequestBody PlaylistDTO playlistDTO){
        return playListService.savePlayList(playlistDTO);
    }


    //PUT
    @PutMapping("/updatePlaylist/{id}")
    private Mono<ResponseEntity<PlaylistDTO>> putPlaylist(@PathVariable String id , @RequestBody PlaylistDTO playlistDTO){
        return playListService.updatePlayList(id,playlistDTO);
    }

    //DELETE
    @DeleteMapping("/deletePlaylist/{id}")
    private Mono<ResponseEntity<String>> deletePlaylist(@PathVariable String id){
        return playListService.deletePlaylist(id);
    }


    @PatchMapping("/addSongPlaylist/{idPlaylist}/{idSong}")
    private Mono<ResponseEntity<PlaylistDTO>> addSongPlaylist(@PathVariable String idPlaylist, @PathVariable String idSong) {
        return songService.findSongById(idSong).switchIfEmpty(Mono.just(ResponseEntity.badRequest().build())).flatMap(dtoResponse -> playListService.addSongPlayList(idPlaylist, dtoResponse.getBody()));
    }

    @PatchMapping("/deleteSongPlaylist/{idPlaylist}/{idSong}")
    private Mono<ResponseEntity<PlaylistDTO>> deleteSongPlaylist(@PathVariable String idPlaylist, @PathVariable String idSong) {
        return songService.findSongById(idSong).switchIfEmpty(Mono.just(ResponseEntity.badRequest().build())).flatMap(songDTOResponse -> playListService.removeSongPlayList(idPlaylist, songDTOResponse.getBody()));
    }


}

