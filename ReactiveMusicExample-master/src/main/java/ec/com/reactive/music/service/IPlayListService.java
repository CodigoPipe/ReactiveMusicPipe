package ec.com.reactive.music.service;

import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Playlist;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPlayListService {

    Mono<ResponseEntity<Flux<PlaylistDTO>>> findAllPlayList();
    Mono<ResponseEntity<PlaylistDTO>> findPlayListById(String id);
    Mono<ResponseEntity<PlaylistDTO>> savePlayList(PlaylistDTO playlistDTO);

    Mono<ResponseEntity<PlaylistDTO>> updatePlayList (String id, PlaylistDTO playlistDTO);

    Mono<ResponseEntity<String>> deletePlaylist (String idPlaylist);

    Mono<ResponseEntity<PlaylistDTO>> addSongPlayList(String idPlaylist, SongDTO songDTO);

    Mono<ResponseEntity<PlaylistDTO>> removeSongPlayList(String idPlaylist, SongDTO songDTO);

    //ModelMapper functions
    Playlist DTOToEntity (PlaylistDTO playlistDTO);
    PlaylistDTO entityToDTO(Playlist playlist);
}
