package ec.com.reactive.music.songtest;


import ec.com.reactive.music.domain.dto.SongDTO;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GetAllSongsNoContentTest {

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
    @DisplayName("findAllSongsError()")
    void findAllSongsError() {

        ResponseEntity<Flux<SongDTO>> songDTOResponse = new ResponseEntity<>(Flux.empty(),HttpStatus.NO_CONTENT);
        Mockito.when(songRepositoryMock.findAll()).thenReturn(Flux.empty());

        var service = songService.findAllSongs();
        StepVerifier.create(service).expectNext(songDTOResponse).expectComplete().verify();

    }
}
