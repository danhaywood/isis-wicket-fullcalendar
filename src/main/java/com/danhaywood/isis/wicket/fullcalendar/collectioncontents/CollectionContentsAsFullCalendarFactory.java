/*
 *  Copyright 2013 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.danhaywood.isis.wicket.fullcalendar.collectioncontents;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import org.apache.isis.applib.filter.Filter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.core.progmodel.facets.value.date.DateValueFacet;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;
import org.apache.isis.viewer.wicket.ui.ComponentFactory;
import org.apache.isis.viewer.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.viewer.wicket.ui.ComponentType;

/**
 * {@link ComponentFactory} for {@link CollectionContentsAsFullCalendar}.
 */
public class CollectionContentsAsFullCalendarFactory extends ComponentFactoryAbstract {

    private static final long serialVersionUID = 1L;

    private static final String NAME = "calendar";

    final static Filter<ObjectAssociation> OF_TYPE_DATE = new Filter<ObjectAssociation>(){

        public boolean accept(final ObjectAssociation objectAssoc) {
            return objectAssoc.getSpecification().containsDoOpFacet(DateValueFacet.class);
        }};

    public CollectionContentsAsFullCalendarFactory() {
        super(ComponentType.COLLECTION_CONTENTS, NAME);
    }

    @Override
    public ApplicationAdvice appliesTo(final IModel<?> model) {
        if(!(model instanceof EntityCollectionModel)) {
            return ApplicationAdvice.DOES_NOT_APPLY;
        }
        final EntityCollectionModel entityCollectionModel = (EntityCollectionModel) model;
        
        // TOFIX: because of Javascript issues, currently only works for standalone views.
        if(!entityCollectionModel.isStandalone()) {
            return ApplicationAdvice.DOES_NOT_APPLY;
        }

        final ObjectSpecification elementSpec = entityCollectionModel.getTypeOfSpecification();
        List<ObjectAssociation> associations = elementSpec.getAssociations(OF_TYPE_DATE);
        return appliesIf(!associations.isEmpty());
    }

    @Override
    public Component createComponent(final String id, final IModel<?> model) {
        final EntityCollectionModel collectionModel = (EntityCollectionModel) model;
        return new CollectionContentsAsFullCalendar(id, collectionModel);
    }
}
