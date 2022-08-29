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
class GetSongByIdTest {


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
    @DisplayName("findSongById()")
    void findSongById() {
        Song songExpected = new Song();
        songExpected.setIdSong("34-766");
        songExpected.setIdAlbum("6546-33");
        songExpected.setLyricsBy("Dorian Black");
        songExpected.setProducedBy("PINA records");
        songExpected.setArrangedBy("COCACOLA");
        songExpected.setDuration(LocalTime.now());

        var songDTOExpected = modelMapper.map(songExpected, SongDTO.class);

        ResponseEntity<SongDTO> songDTOResponse = new ResponseEntity<>(songDTOExpected, HttpStatus.FOUND);

        Mockito.when(songRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.just(songExpected));

        var service = songService.findSongById("34-766");

        StepVerifier.create(service)
                .expectNext(songDTOResponse)
                .expectComplete()
                .verify();


        Mockito.verify(songRepositoryMock).findById("34-766");
    }

}
