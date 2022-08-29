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
public class UpdateSongErrorTest {

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
    @DisplayName("updateSongError()")
    void updateSongError() {

        Song primarySong = new Song();
        primarySong.setIdSong("34-766");
        primarySong.setIdAlbum("6546-33");
        primarySong.setLyricsBy("Dorian Black");
        primarySong.setProducedBy("PINA records");
        primarySong.setArrangedBy("COCACOLA");
        primarySong.setDuration(LocalTime.now());

        var songChanged = primarySong.toBuilder().lyricsBy("Hector lavo").build();
        var songDTOExpected = modelMapper.map(songChanged,SongDTO.class);


        ResponseEntity<SongDTO> songDTOResponse = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        Mockito.when(songRepositoryMock.findById("34-766")).thenReturn(Mono.just(primarySong));

        var service = songService.updateSong("34-766",songDTOExpected);

        StepVerifier.create(service).expectNext(songDTOResponse).expectComplete().verify();
    }


}
