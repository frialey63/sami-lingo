package org.pjp.lingo.view;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import org.pjp.lingo.model.Game;
import org.pjp.lingo.service.CategoryService;
import org.pjp.lingo.service.GameService;

import com.google.common.collect.Sets;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout implements AfterNavigationObserver {

    private static final String ENGLISH_TO_FRENCH = "English to French";

    private static final String FRENCH_TO_ENGLISH = "French to English";

    private static final long serialVersionUID = 4600732259441217958L;

    private static boolean isEmpty(String string) {
        return (string == null) || string.isEmpty();
    }

    private final TextField txtUser = new TextField("Your name");

    private final Select<String> selCategory = new Select<>();

    private final RadioButtonGroup<String> rbgDirection = new RadioButtonGroup<>();

    private final IntegerField fldDifficulty = new IntegerField();

    private final Button btnPlay = new Button("Play");

    private final Button btnReset = new Button("Reset");

    private final GameService gameService;

    private final CategoryService categoryService;

    public MainView(GameService gameService, CategoryService categoryService) {
        this.gameService = gameService;
        this.categoryService = categoryService;

        addClassName("padded-layout");

        NativeLabel lblTitle = new NativeLabel("Sami Lingo");
        lblTitle.addClassNames("h1", "color-blue");

        btnPlay.setEnabled(false);
        btnPlay.addClickListener(l -> play());

        btnReset.setEnabled(false);
        btnReset.addClickListener(l -> reset());

        txtUser.setValueChangeMode(ValueChangeMode.EAGER);
        txtUser.addValueChangeListener(l -> {
            String user = txtUser.getValue();

            boolean resetEnabled = !isEmpty(user);
            btnReset.setEnabled(resetEnabled);

            if (resetEnabled) {
                populateCategories(user);
            }

            boolean playEnabled = resetEnabled && !isEmpty(selCategory.getValue());
            btnPlay.setEnabled(playEnabled);
        });

        selCategory.setLabel("Category");
        selCategory.addValueChangeListener(l -> {
            boolean enabled = !isEmpty(txtUser.getValue()) && !isEmpty(selCategory.getValue());
            btnPlay.setEnabled(enabled);
        });

        rbgDirection.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        rbgDirection.setLabel("Direction");
        rbgDirection.setItems(FRENCH_TO_ENGLISH, ENGLISH_TO_FRENCH);
        rbgDirection.setValue(FRENCH_TO_ENGLISH);

        fldDifficulty.setLabel("Difficulty");
        fldDifficulty.setValue(5);
        fldDifficulty.setStepButtonsVisible(true);
        fldDifficulty.setMin(2);
        fldDifficulty.setMax(9);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(btnPlay, btnReset);

        add(lblTitle, txtUser, selCategory, rbgDirection, fldDifficulty, buttonsLayout);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void reset() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Reset All");
        dialog.setText("Are you sure you want to reset and lose all progress?");

        dialog.setCancelable(true);

        dialog.setConfirmText("Ok");
        dialog.addConfirmListener(l -> {
            String user = txtUser.getValue();

            gameService.clearCompleted(user);

            populateCategories(user);
        });

        dialog.open();
    }

    private void play() {
        UI ui = UI.getCurrent();

        boolean frenchToEnglish = FRENCH_TO_ENGLISH.equals(rbgDirection.getValue());
        Game game = new Game(selCategory.getValue(), frenchToEnglish, fldDifficulty.getValue());

        String user = txtUser.getValue();

        ui.getSession().setAttribute("user", user);
        ui.getSession().setAttribute("game", game);

        ui.navigate(PlayView.class);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String user = (String) UI.getCurrent().getSession().getAttribute("user");

        if (user != null) {
            txtUser.setValue(user);
        }
    }

    private void populateCategories(String user) {
        if (user == null) {
            return;
        }

        Set<String> completedFrenchToEnglish = gameService.getCompleted(user).stream().filter(Game::frenchToEnglish).map(Game::categoryName).collect(toSet());
        Set<String> completedEnglishToFrench = gameService.getCompleted(user).stream().filter(Game::isEnglishToFrench).map(Game::categoryName).collect(toSet());

        Set<String> completed = Sets.newHashSet(completedFrenchToEnglish);
        completed.retainAll(completedEnglishToFrench);

        List<String> items = categoryService.listAll().stream().filter(c -> !completed.contains(c.name())).map(c -> c.name()).toList();
        selCategory.setItems(items);

        if (items.isEmpty()) {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("All Categories Complete");
            dialog.setText("Congratulations!, you have finished playing all categories.");

            dialog.setConfirmText("Ok");
            dialog.addConfirmListener(l -> {
                dialog.close();
            });

            dialog.open();
        }
    }

}
