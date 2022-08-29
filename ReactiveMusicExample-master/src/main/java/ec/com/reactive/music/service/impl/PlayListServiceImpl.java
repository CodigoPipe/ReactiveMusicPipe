package ec.com.reactive.music.service.impl;


import ec.com.reactive.music.domain.dto.PlaylistDTO;

import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Playlist;
import ec.com.reactive.music.repository.IPlayListRepository;
import ec.com.reactive.music.service.IPlayListService;
import ec.com.reactive.music.service.ISongService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PlayListServiceImpl implements IPlayListService {


    @Autowired
    private final IPlayListRepository iPlayListRepository;

    @Autowired
    private final ISongService songService;


    @Autowired
    private final ModelMapper modelMapper;


    @Override
    public Mono<ResponseEntity<Flux<PlaylistDTO>>> findAllPlayList() {
        return this.iPlayListRepository
                .findAll()
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                .map(playList -> entityToDTO(playList))
                .collectList()
                .map(playlistDTOS -> new ResponseEntity<>(Flux.fromIterable(playlistDTOS),HttpStatus.FOUND))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(Flux.empty(),HttpStatus.NO_CONTENT)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> findPlayListById(String id) {
        //Handling errors
        return this.iPlayListRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString()))) //Capture the error
                .map(this::entityToDTO)
                .map(playListDTO -> new ResponseEntity<>(playListDTO, HttpStatus.FOUND)) //Mono<ResponseEntity<AlbumDTO>>
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND))); //Handle the error
    }


    @Override
    public Mono<ResponseEntity<PlaylistDTO>> savePlayList(PlaylistDTO playlistDTO) {
        return this.iPlayListRepository
                .save(DTOToEntity(playlistDTO))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.EXPECTATION_FAILED.toString())))
                .map(playlist -> entityToDTO(playlist))
                .map(playlistDTO1 -> new ResponseEntity<>(playlistDTO1,HttpStatus.CREATED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> updatePlayList(String id, PlaylistDTO playlistDTO) {
        return this.iPlayListRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> {
                    playlistDTO.setIdPlaylist(playlist.getIdPlaylist());
                    return this.savePlayList(playlistDTO);
                })
                .map(playlistDTOResponseEntity -> new ResponseEntity<>(playlistDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }

    @Override
    public Mono<ResponseEntity<String>> deletePlaylist(String idPlaylist) {
        return this.iPlayListRepository
                .findById(idPlaylist)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> this.iPlayListRepository
                        .deleteById(playlist.getIdPlaylist())
                        .map(monoVoid -> new ResponseEntity<>(idPlaylist, HttpStatus.ACCEPTED)))
                .thenReturn(new ResponseEntity<>(idPlaylist, HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> addSongPlayList(String idPL, SongDTO songDTO) {


        return this.iPlayListRepository.findById(idPL)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> {
                    playlist.getSongs().add(songService.dtoToEntity(songDTO));
                    playlist.addDuration(songDTO.getDuration());
                    return this.savePlayList(entityToDTO(playlist));
                }).map(playlistDTOResponseEntity -> new ResponseEntity<>(playlistDTOResponseEntity.getBody(),HttpStatus.OK))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));

    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> removeSongPlayList(String idPL, SongDTO songDTO) {

        return this.iPlayListRepository.findById(idPL)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> {
                    playlist.getSongs().remove(songService.dtoToEntity(songDTO));
                    playlist.reduceDuration(songDTO.getDuration());
                    return this.savePlayList(entityToDTO(playlist));
                }).map(playlistDTOResponseEntity -> new ResponseEntity<>(playlistDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));


    }

    @Override
    public Playlist DTOToEntity(PlaylistDTO playlistDTO) {
        return this.modelMapper.map(playlistDTO, Playlist.class);
    }

    @Override
    public PlaylistDTO entityToDTO(Playlist playlist) {
        return this.modelMapper.map(playlist, PlaylistDTO.class);
    }





    /*return iPlayListRepository.findById(idPlaylist)
            .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
            .flatMap(playlist -> songService
            .findSongById(idSong)
            .flatMap(responseEntity -> {
        ArrayList<Song> songsList = playlist.getSongs();
        Song song = songService.dtoToEntity(responseEntity.getBody());
        songsList.add(song);
        LocalTime songDuration = song.getDuration();
        LocalTime playListDuration = playlist.getDuration()
                .plusHours(songDuration.getHour())
                .plusMinutes(songDuration.getMinute())
                .plusSeconds(songDuration.getSecond());
        playlist.setDuration(playListDuration);
        return iPlayListRepository.save(playlist);}))
            .map(this::entityToDTO)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.ACCEPTED))
            .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)));*/
}
