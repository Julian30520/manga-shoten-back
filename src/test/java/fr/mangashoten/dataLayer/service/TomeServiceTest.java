package fr.mangashoten.dataLayer.service;

import fr.mangashoten.dataLayer.DataLayerApplication;
import fr.mangashoten.dataLayer.model.Editor;
import fr.mangashoten.dataLayer.model.Tome;
import fr.mangashoten.dataLayer.repository.TomeRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataLayerApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TomeServiceTest {

    @Autowired
    private TomeRepository tomeRepository;

    @Autowired
    private TomeService tomeService;

    @Test
    void getTomesTest() {
        // GIVEN
        List<Tome> initTomes = new ArrayList<>();

        for (int index = 1; index <= 2; index++) {
            Tome tomeToSend = new Tome();
            tomeToSend.setTomeId(index);
            tomeToSend.setTomeNumber(1);
            tomeToSend.setPageNumber(250);
            tomeToSend.setCover("urlImage");
            tomeToSend.setEditor(new Editor());
            tomeToSend.setUsers(new ArrayList<>());

            Tome genericTome = tomeRepository.save(tomeToSend);
            initTomes.add(tomeToSend);
        }

        // WHEN
        Iterable<Tome> foundTomes = tomeService.getTomes();
        List<Tome> result = new ArrayList<>();
        foundTomes.forEach(tome -> result.add(tome));

        // THEN
        assertEquals(initTomes.get(0).getTomeId(), result.get(0).getTomeId());
        assertEquals(initTomes.get(1).getTomeId(), result.get(1).getTomeId());
    }

    @Test
    void getTomeByIdTest() {
        // GIVEN
        Tome tomeToSend = new Tome();
        tomeToSend.setTomeId(1);
        tomeToSend.setTomeNumber(1);
        tomeToSend.setPageNumber(250);
        tomeToSend.setCover("urlImage");
        tomeToSend.setEditor(new Editor());
        tomeToSend.setUsers(new ArrayList<>());

        Tome genericTome = tomeRepository.save(tomeToSend);

        // WHEN
        Optional<Tome> foundTome = tomeService.getTomeById(genericTome.getTomeId());

        // THEN
        assertEquals(genericTome.getTomeId(), foundTome.get().getTomeId());
    }

    @Test
    void addTomeTest() {
        // GIVEN
        Tome tomeToSend = new Tome();
        tomeToSend.setTomeId(1);
        tomeToSend.setTomeNumber(1);
        tomeToSend.setPageNumber(250);
        tomeToSend.setCover("urlImage");
        tomeToSend.setEditor(new Editor());
        tomeToSend.setUsers(new ArrayList<>());

        // WHEN
        Tome genericTome = tomeService.addTome(tomeToSend);

        // THEN
        assertEquals(tomeToSend.getTomeId(), genericTome.getTomeId());
    }

    @Test
    void deleteTomeTest() {
        // GIVEN
        Tome tomeToSend = new Tome();
        tomeToSend.setTomeId(1);
        tomeToSend.setTomeNumber(1);
        tomeToSend.setPageNumber(250);
        tomeToSend.setCover("urlImage");
        tomeToSend.setEditor(new Editor());
        tomeToSend.setUsers(new ArrayList<>());

        Tome genericTome = tomeRepository.save(tomeToSend);

        // WHEN
        tomeService.deleteTome(genericTome);

        // THEN
        assertEquals(1, genericTome.getTomeId());
        assertEquals(Optional.empty(), tomeRepository.findById(1));
    }
}