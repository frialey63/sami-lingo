package org.pjp.lingo.view;

import org.pjp.lingo.service.CategoryService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route
public class MainView extends VerticalLayout implements AfterNavigationObserver {

    private static final long serialVersionUID = 4600732259441217958L;

    private static boolean isEmptyString(String string) {
        return string == null || string.isEmpty();
    }

    private final TextField txtName = new TextField("Your name");

    private final Select<String> selCategory = new Select<>();

    private final Button btnPlay = new Button("Play", l -> {
        UI.getCurrent().navigate(PlayView.class);
    });

    private final CategoryService categoryService;

    public MainView(CategoryService categoryService) {
        this.categoryService = categoryService;

        addClassName("padded-layout");

        NativeLabel lblTitle = new NativeLabel("Sami Lingo");
        lblTitle.addClassNames("h1", "color-blue");

        btnPlay.setEnabled(false);

        Button btnReset = new Button("Reset Game");

        txtName.setValueChangeMode(ValueChangeMode.EAGER);
        txtName.addKeyUpListener(l -> validate());

        selCategory.setLabel("Category");
        selCategory.addValueChangeListener(l -> validate());

        RadioButtonGroup<String> rbgDirection = new RadioButtonGroup<>();
        rbgDirection.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        rbgDirection.setLabel("Direction");
        rbgDirection.setItems("French to English", "English to French");
        rbgDirection.setValue("French to English");

        IntegerField fldDifficulty = new IntegerField();
        fldDifficulty.setLabel("Difficulty");
        fldDifficulty.setValue(5);
        fldDifficulty.setStepButtonsVisible(true);
        fldDifficulty.setMin(2);
        fldDifficulty.setMax(9);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(btnPlay, btnReset);

        add(lblTitle, txtName, selCategory, rbgDirection, fldDifficulty, buttonsLayout);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        selCategory.setItems(categoryService.list().stream().map(c -> c.name()).toList());
    }

    private void validate() {
        boolean valid = !isEmptyString(txtName.getValue()) && !isEmptyString(selCategory.getValue());

        btnPlay.setEnabled(valid);
    }
}
