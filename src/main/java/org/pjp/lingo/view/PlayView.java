package org.pjp.lingo.view;

import org.pjp.lingo.model.Challenge;
import org.pjp.lingo.model.Game;
import org.pjp.lingo.model.Progress;
import org.pjp.lingo.service.CategoryService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route
public class PlayView extends VerticalLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = 8740422261833804095L;

    private final NativeLabel lblUser = new NativeLabel();

    private final NativeLabel lblProgress = new NativeLabel();

    private final NativeLabel lblScore = new NativeLabel();

    private final NativeLabel lblCategory = new NativeLabel();

    private final RadioButtonGroup<String> rbgQuestion = new RadioButtonGroup<>();

    private final Button btnChoose = new Button("Choose");

    private final Button btnSkip = new Button("Skip");

    private final Button btnReset = new Button("Reset");

    private final Button btnQuit = new Button("Quit");

    private final CategoryService categoryService;

    private final Progress progress = new Progress();

    private Challenge challenge;

    private int numMistakes;

    private int numCorrect;

    private Game game;

    public PlayView(CategoryService categoryService) {
        this.categoryService = categoryService;

        addClassName("padded-layout");

        NativeLabel lblTitle = new NativeLabel("Sami Lingo");
        lblTitle.addClassNames("h1", "color-blue");

        lblUser.addClassName("h3");
        lblProgress.addClassName("h3");
        lblScore.addClassName("h3");
        lblCategory.addClassName("h2");

        rbgQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        btnChoose.setEnabled(false);
        btnChoose.addClickListener(l -> chooseOption());

        btnSkip.setEnabled(false);
        btnSkip.addClickListener(l -> skip());

        btnReset.setEnabled(false);
        btnReset.addClickListener(l -> resetGame());

        btnQuit.setEnabled(false);
        btnQuit.addClickListener(l -> quitGame());

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(btnChoose, btnSkip, btnReset, btnQuit);

        add(lblTitle, lblUser, lblProgress, lblScore, lblCategory, rbgQuestion, buttonsLayout);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.END, lblUser, lblProgress, lblScore);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        VaadinSession session = UI.getCurrent().getSession();

        String user = (String) session.getAttribute("user");
        game = (Game) session.getAttribute("game");

        lblUser.setText("User: " + user);
        lblProgress.setText(formatProgress());
        lblScore.setText(formatScore());
        lblCategory.setText(game.categoryName());

        nextChallenge();

        btnChoose.setEnabled(true);
        btnSkip.setEnabled(true);
        btnReset.setEnabled(true);
        btnQuit.setEnabled(true);
    }

    private void nextChallenge() {
        challenge = categoryService.generateChallenge(game.categoryName(), game.frenchToEnglish(), game.difficulty(), progress);

        if (challenge != null) {
            rbgQuestion.setLabel(challenge.target());
            rbgQuestion.setItems(challenge.options());
        } else {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Category Complete");
            dialog.setText("Congratulations!, you have finished playing this category.");

            dialog.setConfirmText("Ok");
            dialog.addConfirmListener(l -> {
                UI.getCurrent().navigate(MainView.class);
            });

            dialog.open();
        }
    }

    private void chooseOption() {
        String chosen = rbgQuestion.getValue();
        String wanted = challenge.options().get(challenge.answer());

        if (chosen.equals(wanted)) {
            String target = rbgQuestion.getLabel();
            progress.update(target);

            numCorrect++;
        } else {
            numMistakes++;
        }

        nextChallenge();

        lblProgress.setText(formatProgress());
        lblScore.setText(formatScore());
    }

    private void skip() {
        nextChallenge();
    }

    private void resetGame() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Reset Game");
        dialog.setText("Are you sure you want to reset the game and lose all progress made in this category?");

        dialog.setCancelable(true);

        dialog.setConfirmText("Ok");
        dialog.addConfirmListener(l -> {
            progress.reset();
            numCorrect = numMistakes = 0;

            lblProgress.setText(formatProgress());
            lblScore.setText(formatScore());

            nextChallenge();
        });

        dialog.open();
    }

    private void quitGame() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Quit Game");
        dialog.setText("Are you sure you want to quit the game and lose all progress made in this category?");

        dialog.setCancelable(true);

        dialog.setConfirmText("Ok");
        dialog.addConfirmListener(l -> {
            UI.getCurrent().navigate(MainView.class);
        });

        dialog.open();
    }

    private String formatScore() {
        return String.format("Score: %d \u2713 %d \u2717", numCorrect, numMistakes);
    }

    private String formatProgress() {
        int score = categoryService.calculateProgress(game.categoryName(), progress);
        return String.format("Progress: %d%%", score);
    }

}
