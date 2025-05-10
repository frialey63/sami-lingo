package org.pjp.lingo.view;

import org.pjp.lingo.model.Challenge;
import org.pjp.lingo.model.Progress;
import org.pjp.lingo.service.CategoryService;

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

@Route
public class PlayView extends VerticalLayout implements AfterNavigationObserver {

    /**
     *
     */
    private static final long serialVersionUID = 8740422261833804095L;

    private static final String CATEGORY = "Household Chores";

    private final NativeLabel lblUser = new NativeLabel();

    private final NativeLabel lblProgress = new NativeLabel();

    private final NativeLabel lblScore = new NativeLabel();

    private final NativeLabel lblCategory = new NativeLabel(CATEGORY);

    private final RadioButtonGroup<String> rbgQuestion = new RadioButtonGroup<>();

    private final Button btnChoose = new Button("Choose");

    private final Button btnSkip = new Button("Skip");

    private final Button btnReset = new Button("Reset Category");

    private final CategoryService categoryService;

    private final Progress progress = new Progress();

    private Challenge challenge;

    private int numMistakes;

    private int numCorrect;

    public PlayView(CategoryService categoryService) {
        this.categoryService = categoryService;

        addClassName("padded-layout");

        NativeLabel lblTitle = new NativeLabel("Sami Lingo");
        lblTitle.addClassNames("h1", "color-blue");

        lblUser.setText("User: PAUL");
        lblUser.addClassName("h3");

        lblProgress.setText(formatProgress());
        lblProgress.addClassName("h3");

        lblScore.setText(formatScore());
        lblScore.addClassName("h3");

        lblCategory.addClassName("h2");
        lblCategory.setText(CATEGORY);

        rbgQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        btnChoose.setEnabled(false);
        btnChoose.addClickListener(l -> chooseOption());

        btnSkip.setEnabled(false);
        btnSkip.addClickListener(l -> skip());

        btnReset.setEnabled(false);
        btnReset.addClickListener(l -> resetGame());

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(btnChoose, btnSkip, btnReset);

        add(lblTitle, lblUser, lblProgress, lblScore, lblCategory, rbgQuestion, buttonsLayout);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.END, lblUser, lblProgress, lblScore);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        nextChallenge();

        btnChoose.setEnabled(true);
        btnSkip.setEnabled(true);
        btnReset.setEnabled(true);
    }

    private void nextChallenge() {
        challenge = categoryService.generateChallenge(CATEGORY, true, 5, progress);

        if (challenge != null) {
            rbgQuestion.setLabel(challenge.target());
            rbgQuestion.setItems(challenge.options());
        } else {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Game Over");
            dialog.setText("You have finished playing this category.");

            dialog.setConfirmText("Ok");
            dialog.addConfirmListener(l -> {
                btnChoose.setEnabled(false);
                btnSkip.setEnabled(false);
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

    private String formatScore() {
        return String.format("Score: %d \u2713 %d \u2717", numCorrect, numMistakes);
    }

    private String formatProgress() {
        int score = categoryService.calculateProgress(CATEGORY, progress);
        return String.format("Progress: %d%%", score);
    }

}
