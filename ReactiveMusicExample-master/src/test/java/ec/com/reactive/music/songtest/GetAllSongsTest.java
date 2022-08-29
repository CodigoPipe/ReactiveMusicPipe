package ec.com.reactive.music.songtest;


import ec.com.reactive.music.domain.dto.AlbumDTO;
import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Album;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class GetAllSongsTest {

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
    @DisplayName("findAllSongs()")
    void findAllSongs() {

        ArrayList<Song> listSongs = new ArrayList<>();
        listSongs.add(new Song());
        listSongs.add(new Song());
        listSongs.add(new Song());

        ArrayList<SongDTO> listSongsDTO = listSongs.stream().map(song -> modelMapper.map(song, SongDTO.class)).collect(Collectors.toCollection(ArrayList::new));

        var fluxResult = Flux.fromIterable(listSongs);
        var fluxResultDTO = Flux.fromIterable(listSongsDTO);


        ResponseEntity<Flux<SongDTO>> rEntResult = new ResponseEntity<>(fluxResultDTO, HttpStatus.FOUND);

        //3. Mockeo - Mockear el resultado esperado
        Mockito.when(songRepositoryMock.findAll()).thenReturn(fluxResult);

        //4. Servicio
        var service = songService.findAllSongs();

        //5. Stepverifier
        StepVerifier.create(service)
                .expectNextMatches(fluxResponseEntity -> fluxResponseEntity.getStatusCode().is3xxRedirection())
                .expectComplete().verify();

    }
}
