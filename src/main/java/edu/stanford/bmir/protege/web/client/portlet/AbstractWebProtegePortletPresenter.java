package edu.stanford.bmir.protege.web.client.portlet;

import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


public abstract class AbstractWebProtegePortletPresenter implements WebProtegePortletPresenter {

    private final SelectionModel selectionModel;

    private final ProjectId projectId;

    private final HandlerRegistration selectionModelHandlerRegistration;

    private Optional<PortletUi> portletUi = Optional.empty();

    public AbstractWebProtegePortletPresenter(@Nonnull SelectionModel selectionModel,
                                              @Nonnull ProjectId projectId) {
        this.selectionModel = checkNotNull(selectionModel);
        this.projectId = checkNotNull(projectId);

        // TODO: Move this out
        selectionModelHandlerRegistration = selectionModel.addSelectionChangedHandler(e -> {
//                if (portletUi.asWidget().isAttached()) {
                    handleBeforeSetEntity(e.getPreviousSelection());
                    handleAfterSetEntity(e.getLastSelection());
//                }
            }
        );
    }

    @Override
    public final void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = Optional.of(portletUi);
        startPortlet(portletUi, eventBus);
    }

    public abstract void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus);

    public SelectionModel getSelectionModel() {
        return selectionModel;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    protected void setViewTitle(String title) {
        portletUi.ifPresent(ui -> ui.setViewTitle(title));
    }

    protected void setForbiddenVisible(boolean forbiddenVisible) {
        portletUi.ifPresent(ui -> ui.setForbiddenVisible(forbiddenVisible));
    }

    protected void handleBeforeSetEntity(Optional<? extends OWLEntity> existingEntity) {
    }

    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Optional<OWLEntity> getSelectedEntity() {
        return getSelectionModel().getSelection();
    }


    @Override
    public String toString() {
        return toStringHelper("EntityPortlet")
                .addValue(getClass().getName())
                .toString();
    }

    @Override
    public void dispose() {
        selectionModelHandlerRegistration.removeHandler();
    }
}