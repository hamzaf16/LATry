/**
 * Copyright 2010 http://learning-adventure.fr
 * Tous droits réservés
 * 
 * 
 * ----------------------------------------------------------------------------
 * Ce fichier fait partie de LA-Client.
 *
 * LA-Client est un logiciel libre ; vous pouvez le redistribuer ou le modifier 
 * suivant les termes de la GNU General Public License telle que publiée par
 * la Free Software Foundation ; soit la version 3 de la licence, soit 
 * (à votre gré) toute version ultérieure.
 * 
 * LA-Client est distribué dans l'espoir qu'il sera utile, 
 * mais SANS AUCUNE GARANTIE ; pas même la garantie implicite de 
 * COMMERCIABILISABILITÉ ni d'ADÉQUATION à UN OBJECTIF PARTICULIER. 
 * Consultez la GNU General Public License pour plus de détails.
 * 
 * Vous devez avoir reçu une copie de la GNU General Public License 
 * en même temps que LA-Client ; si ce n'est pas le cas, 
 * consultez <http://www.gnu.org/licenses>.
 * ----------------------------------------------------------------------------
 * This file is part of LA-Client.
 *
 * LA-Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LA-Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LA-Client.  If not, see <http://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------------
 */
package client.hud.editor.annotation;

import java.lang.reflect.Field;

import client.editor.annotation.AnnotationEditor;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.interfaces.network.Sharable;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.TableLayout;

/**
 * Permet l'edition de code javascript
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class EditableEdit extends BContainer {
	private AnnotationEditor editor;

	/**
	 * 
	 */
	public EditableEdit(final EditAnnotationWindow window,final Field f) {
		super();
		TableLayout layout = new TableLayout(2,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		this.setLayoutManager(layout);

		BLabel label = new BLabel(f.getName());
		label.setFit(Fit.SCALE);
		this.add(label);

		this.editor = window.getEditor();

		LaBButton button = new LaBButton("edit");
		button.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				window.getHud().getGame().getServerEditor().edit(
						((Sharable)editor.get(f)).getKey());
			}
		});
		this.add(button);

	}

}
