package ec.com.reactive.music.service.impl;





import ec.com.reactive.music.domain.entities.Album;
import ec.com.reactive.music.domain.entities.Playlist;
import ec.com.reactive.music.domain.entities.Song;
import ec.com.reactive.music.repository.IPlayListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class DeletePlaylistTest {

    @Mock
    IPlayListRepository playListRepositoryMock;

    ModelMapper modelMapper; //Helper - Apoyo/Soporte

    PlayListServiceImpl playListService;

    SongServiceImpl songService;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        playListService = new PlayListServiceImpl(playListRepositoryMock, songService ,modelMapper);
    }

    @Test
    @DisplayName("deletePlaylist()")
    void deletePlaylist(){

        ArrayList<Song> songs = new ArrayList<>();

        Playlist playlistExpected = new Playlist();
        playlistExpected.setIdPlaylist("12345678-9");
        playlistExpected.setName("playlistTesting");
        playlistExpected.setUsername("probando username");
        playlistExpected.setSongs(songs);
        playlistExpected.setDuration(LocalTime.now());


        ResponseEntity<String> responseDelete = new ResponseEntity<>(playlistExpected.getIdPlaylist(), HttpStatus.ACCEPTED);

        Mockito.when(playListRepositoryMock.findById(Mockito.any(String.class)))
                .thenReturn(Mono.just(playlistExpected));
        Mockito.when(playListRepositoryMock.deleteById(Mockito.any(String.class)))
                .thenReturn(Mono.empty());


        var service = playListService.deletePlaylist("12345678-9");


        StepVerifier.create(service).expectNext(responseDelete).expectComplete().verify();

        Mockito.verify(playListRepositoryMock).findById("12345678-9");
        Mockito.verify(playListRepositoryMock).deleteById("12345678-9");

    }


}
