package org.pjp.lingo.view;

import org.pjp.lingo.model.Category;
import org.pjp.lingo.service.CategoryService;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class AdminView extends VerticalLayout {

    /**
     *
     */
    private static final long serialVersionUID = 8713084370688369200L;

    public AdminView(CategoryService categoryService) {

        addClassName("padded-layout");

        NativeLabel lblTitle = new NativeLabel("Sami Lingo");
        lblTitle.addClassNames("h1", "color-blue");

        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();

        TextField txtCategory = new TextField("Category");

        Button btnLoad = new Button("Load Category", e -> {
            String name = txtCategory.getValue();

            categoryService.loadCategory(name);
        });
        btnLoad.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLoad.addClickShortcut(Key.ENTER);

        Button btnList = new Button("List Category", e -> {
            String name = txtCategory.getValue();

            Category category = categoryService.getCategory(name);

            if (category != null) {
                category.definitions().forEach(d -> right.add(new Paragraph(d.french() + " = " + d.english())));
            }
        });
        btnList.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnList.addClickShortcut(Key.ENTER);

        left.add(txtCategory, btnLoad, btnList);
        left.setDefaultHorizontalComponentAlignment(Alignment.START);

        SplitLayout splitLayout = new SplitLayout(left, right);

        add(lblTitle, splitLayout);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setHorizontalComponentAlignment(Alignment.STRETCH, splitLayout);
    }
}
