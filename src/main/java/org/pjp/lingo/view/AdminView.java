package org.pjp.lingo.view;

import org.pjp.lingo.model.Category;
import org.pjp.lingo.service.CategoryService;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class AdminView extends VerticalLayout {

    private static final long serialVersionUID = 8713084370688369200L;

    private static boolean isEmpty(String string) {
        return (string == null) || string.isEmpty();
    }

    private TextField txtCategory = new TextField("Category");

    private VerticalLayout left = new VerticalLayout();

    private VerticalLayout right = new VerticalLayout();

    private final CategoryService categoryService;

    public AdminView(CategoryService categoryService) {
        this.categoryService = categoryService;

        addClassName("padded-layout");

        NativeLabel lblTitle = new NativeLabel("Sami Lingo");
        lblTitle.addClassNames("h1", "color-blue");

        Button btnLoad = new Button("Load Category", e -> loadCategory());
        btnLoad.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLoad.addClickShortcut(Key.ENTER);

        Button btnList = new Button("List Category", e -> listCategory());
        btnList.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnList.addClickShortcut(Key.ENTER);

        left.add(txtCategory, btnLoad, btnList);
        left.setDefaultHorizontalComponentAlignment(Alignment.START);

        SplitLayout splitLayout = new SplitLayout(left, right);

        add(lblTitle, splitLayout);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.STRETCH, splitLayout);
    }

    private void listCategory() {
        String name = txtCategory.getValue();

        if (!isEmpty(name)) {
            Category category = categoryService.getCategory(name);

            if (category != null) {
                right.removeAll();
                category.definitions().forEach(d -> right.add(new Paragraph(d.french() + " = " + d.english())));
            }
        }
    }

    private void loadCategory() {
        String name = txtCategory.getValue();

        if (!isEmpty(name)) {
            int numberOfDefinitions = categoryService.loadCategory(name);
            Notification.show(String.format("Loaded %d defintions for this category", numberOfDefinitions));
        }
    }
}
