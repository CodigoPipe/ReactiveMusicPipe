package ec.com.reactive.music.songtest;


import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Song;
import ec.com.reactive.music.repository.ISongRepository;
import ec.com.reactive.music.service.impl.SongServiceImpl;
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

@ExtendWith(MockitoExtension.class)
class DeleteSongErrorTest {


    @Mock
    ISongRepository songRepositoryMock;

    ModelMapper modelMapper;

    SongServiceImpl songService;

    @BeforeEach
    void init() {
        modelMapper = new ModelMapper();
        songService = new SongServiceImpl(songRepositoryMock, modelMapper);
    }

    @Test
    @DisplayName("deleteSongError()")
    void deleteSongError() {

        Song primarySong = new Song();
        primarySong.setIdSong("34-766");
        primarySong.setIdAlbum("6546-33");
        primarySong.setLyricsBy("Dorian Black");
        primarySong.setProducedBy("PINA records");
        primarySong.setArrangedBy("COCACOLA");
        primarySong.setDuration(LocalTime.now());

        ResponseEntity<String> songDTOResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Mockito.when(songRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var service = songService.deleteSong("34-766");

        StepVerifier.create(service).expectNext(songDTOResponse).expectComplete().verify();

        Mockito.verify(songRepositoryMock).findById("34-766");

    }
}
