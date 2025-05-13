package org.pjp.lingo.service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.pjp.lingo.model.Category;
import org.pjp.lingo.model.Challenge;
import org.pjp.lingo.model.Definition;
import org.pjp.lingo.model.Progress;
import org.pjp.lingo.storage.CategoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private static final String CSV = ".csv";

    private final CategoryStorage categoryStorage;

    private final Random random = new Random();

    public CategoryServiceImpl(CategoryStorage categoryStorage) {
        this.categoryStorage = categoryStorage;
    }

    @Override
    public List<Category> listAll() {
        return categoryStorage.getAll().stream().sorted().toList();
    }

    @Override
    public int loadCategory(String name) {
        int result = 0;

        Path csvFile = Path.of(name + CSV);

        try {
            List<List<String>> records = new ArrayList<List<String>>();

            try (CSVReader csvReader = new CSVReader(new FileReader(csvFile.toFile()));) {
                String[] values = null;

                while ((values = csvReader.readNext()) != null) {
                    records.add(Arrays.asList(values));
                }
            }

            List<Definition> definitions = records.stream().map(r -> new Definition(r.get(0), r.get(1))).toList();

            Category category = new Category(name, definitions);

            categoryStorage.save(category);

            result = definitions.size();

        } catch (CsvValidationException  | IOException e) {
            LOGGER.error("Failed to load category", e);
        }

        return result;
    }

    @Override
    public Category getCategory(String name) {
        return categoryStorage.load(name);
    }

    @Override
    public Challenge generateChallenge(String name, boolean french, int numOptions, Progress progress) {
        Category category = getCategory(name);

        if ((category != null) && (numOptions <= category.definitions().size())) {
            boolean english = !french;

            List<Definition> list = category.definitions().stream().filter(d -> progress.excludes(d.getWord(french))).toList();

            if (list.size() > 0) {
                // generate the target
                Definition target = list.get(random.nextInt(list.size()));

                // generate the options

                list = category.definitions().stream().filter(d -> !d.getWord(french).equals(target.getWord(french))).collect(Collectors.toList());
                Collections.shuffle(list);

                List<String> options = Stream.concat(list.stream().limit(numOptions - 1).map(d -> d.getWord(english)), Stream.of(target.getWord(english))).collect(Collectors.toList());
                Collections.shuffle(options);

                // specify the answer
                int answer = options.indexOf(target.getWord(english));

                return new Challenge(target.getWord(french), options, answer);
            }
        } else {
            LOGGER.warn("Cannot generate a challenge, not enough defintions in this category for the number of options.");
        }

        return null;
    }

    @Override
    public int calculateProgress(String name, Progress progress) {
        Category category = getCategory(name);

        if (category != null) {
            return Math.round((100.0f * progress.count()) / category.definitions().size());
        }

        return 0;
    }

}
