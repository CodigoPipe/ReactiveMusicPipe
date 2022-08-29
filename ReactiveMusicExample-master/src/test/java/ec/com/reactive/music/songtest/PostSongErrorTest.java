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
class PostSongErrorTest {

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
    @DisplayName("postSongError()")
    void postSongError() {

        SongDTO songExpected = new SongDTO();
        songExpected.setIdSong("34-766");
        songExpected.setIdAlbum("6546-33");
        songExpected.setLyricsBy("Dorian Black");
        songExpected.setProducedBy("PINA records");
        songExpected.setArrangedBy("COCACOLA");
        songExpected.setDuration(LocalTime.now());

        ResponseEntity<SongDTO> songDTOResponse = new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        Mockito.when(songRepositoryMock.save(Mockito.any(Song.class))).thenReturn(Mono.empty());

        var service = songService.saveSong(songExpected);

        StepVerifier.create(service).expectNext(songDTOResponse).expectComplete().verify();
    }
}
