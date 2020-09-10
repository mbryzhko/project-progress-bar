package com.bma.ppb.ui.view;

import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.repository.ProjectEndState;
import com.bma.ppb.project.service.ProjectService;
import com.bma.ppb.timeline.model.ReportingPeriodBean;
import com.bma.ppb.ui.data.ProjectViewDataProvider;
import com.bma.ppb.project.repository.ProgressState;
import com.bma.ppb.timeline.service.TimelineService;
import com.bma.ppb.ui.model.ProjectViewBean;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

@Route(value = "")
@PWA(name = "Project Progress Bar", shortName = "ppb")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@CssImport(value = "./styles/views/project-progress-bar-view.css", include = "lumo-badge")
@JsModule("./styles/shared-styles.js")
@Slf4j
public class ProgressBarView extends VerticalLayout {

    private final ProjectService projectService;
    private final TimelineService timelineService;
    private final ProjectViewDataProvider dataProvider;

    private Editor<ProjectViewBean> projectEditor;
    private Collection<Button> editButtons = Collections.newSetFromMap(new WeakHashMap<>());
    private ReportingPeriodBean reportingPeriod;

    private static final String NAME_COL_WIDTH = "25%";
    private static final String PROGRESS_BAR_COL_WIDTH = "50%";
    private static final String END_STATE_COL_WIDTH = "13%";
    private static final String ACTIONS_COL_WIDTH = "7%";

    public ProgressBarView(ProjectViewDataProvider dataProvider, ProjectService projectService, TimelineService timelineService) {
        this.dataProvider = dataProvider;
        this.projectService = projectService;
        this.timelineService = timelineService;

        reportingPeriod = timelineService.getCurrentReportingPeriod();

        add(createToolBar());
        add(projectsGrid());
    }

    private Component projectsGrid() {

        Grid<ProjectViewBean> projectGrid = new Grid<>(ProjectViewBean.class, false);
        projectGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        projectGrid.setHeightByRows(true);
        projectGrid.setDataProvider(dataProvider);

        Binder<ProjectViewBean> binder = new Binder<>(ProjectViewBean.class);
        projectEditor = projectGrid.getEditor();
        projectEditor.setBinder(binder);
        projectEditor.setBuffered(true);
        projectEditor.addOpenListener(e -> editButtons.forEach(button -> button.setEnabled(!projectEditor.isOpen())));
        projectEditor.addCloseListener(e -> editButtons.forEach(button -> button.setEnabled(!projectEditor.isOpen())));
        projectEditor.addSaveListener(this::updateProject);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        projectGrid.addColumn(ProjectViewBean::getName)
                .setHeader("Name")
                .setWidth(NAME_COL_WIDTH)
                .setEditorComponent(createNameColumnEditor(binder, validationStatus));

        projectGrid.addComponentColumn(this::createProgressBar)
                .setHeader("Progress Bar")
                .setWidth(PROGRESS_BAR_COL_WIDTH)
                .setEditorComponent(item -> createProgressBarEditor(binder, validationStatus));

        projectGrid.addColumn(ProjectViewBean::getEndState)
                .setHeader("Committed End State")
                .setWidth(END_STATE_COL_WIDTH)
                .setEditorComponent(createEndStateEditor(binder));

        projectGrid.addComponentColumn(this::editButton)
                .setTextAlign(ColumnTextAlign.END)
                .setWidth(ACTIONS_COL_WIDTH)
                .setEditorComponent(createActionEditorButtons());

        return projectGrid;
    }

    private Component createProgressBarEditor(Binder<ProjectViewBean> binder, Div validationStatus) {

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPlaceholder("Start Date");
        binder.bind(startDatePicker, "startDate");

        DatePicker completeDatePicker = new DatePicker();
        completeDatePicker.setPlaceholder("Complete Date");
        binder.bind(completeDatePicker, "completeDate");

        NumberField progressField = new NumberField();
        progressField.setPlaceholder("%");
        progressField.setMax(100);
        progressField.setMin(0);
        progressField.setStep(5);
        progressField.setHasControls(true);
        binder.forField(progressField)
                .withValidator(new DoubleRangeValidator("Project progress must be between 0% and 100%", 0.0, 100.0))
                .withStatusLabel(validationStatus).bind("progressPercent");

        Select<ProgressState> progressState = new Select<>();
        progressState.setItems(ProgressState.values());
        binder.bind(progressState, "progressState");

        return new HorizontalLayout(startDatePicker, completeDatePicker, progressField, progressState);
    }

    private Component createActionEditorButtons() {
        Button save = new Button(new Icon(VaadinIcon.CHECK), e -> projectEditor.save());
        Button cancel = new Button(new Icon(VaadinIcon.CLOSE), e -> projectEditor.cancel());
        return new Div(save, cancel);
    }

    private Component createEndStateEditor(Binder<ProjectViewBean> binder) {
        Select<ProjectEndState> endStateSelect = new Select<>();
        endStateSelect.setItems(ProjectEndState.values());
        binder.bind(endStateSelect, "endState");
        return endStateSelect;
    }

    private Component createNameColumnEditor(Binder<ProjectViewBean> binder, Div validationStatus) {
        TextField nameField = new TextField();
        nameField.setSizeFull();
        binder.forField(nameField)
                .withValidator(new StringLengthValidator("Project name length must be between 3 and 255.", 3, 255))
                .withStatusLabel(validationStatus).bind("name");
        return nameField;
    }

    private Component editButton(ProjectViewBean project) {
        if (project.getKind() == ProjectViewBean.Kind.PROJECT) {
            Button editBtn = new Button(new Icon(VaadinIcon.EDIT), btn -> projectEditor.editItem(project));
            editButtons.add(editBtn);
            return editBtn;
        } else {
            return new Label();
        }
    }

    private Component createProgressBar(ProjectViewBean item) {
        return item.getRenderer().renderProgressBar(item, reportingPeriod);
    }

    private Component createToolBar() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(applicationTitle());
        layout.add(addProjectButton());
        return layout;
    }

    private Component addProjectButton() {
        Button button = new Button(new Icon(VaadinIcon.PLUS_CIRCLE_O), this::addNewProject);
        button.setClassName("add-new-project-btn");
        return button;
    }

    private void addNewProject(ClickEvent<Button> event) {
        projectService.createProject(ProjectBean.builder().name("New Project Name " + System.currentTimeMillis()).build());
        dataProvider.refreshAll();
    }

    private void updateProject(EditorSaveEvent<ProjectViewBean> event) {
        ProjectViewBean updatedProjectView = event.getItem();
        projectService.updateProject(ProjectBean.builder()
                .id(updatedProjectView.getId())
                .name(updatedProjectView.getName())
                .startDate(updatedProjectView.getStartDate())
                .completeDate(updatedProjectView.getCompleteDate())
                .progressState(updatedProjectView.getProgressState())
                .endState(updatedProjectView.getEndState())
                .progressPercent(updatedProjectView.getProgressPercent()).build());
    }

    private Component applicationTitle() {
        Label label = new Label("Conductor Roadmap Progress");
        label.setClassName("app-title");
        return label;
    }
}
