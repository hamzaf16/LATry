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
package client.map.tool.blackboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Un rectangle.
 * 
 * @author Syscom
 */
public class Rect extends AbstractFigure {
	public Rect(int id, int x0, int y0, int x1, int y1, Color color,
			boolean fill, int thickness) {
		super(id, x0, y0, x1, y1, color, fill, thickness);
	}

	public Rect(int id, Point p0, Point p1, Color color, boolean fill,
			int thickness) {
		super(id, p0.x, p0.y, p1.x, p1.y, color, fill, thickness);
	}

	@Override
	protected void draw(Graphics2D g) {
		if (fillPoly)
			g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		else
			g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public String toString() {
		return "rect¤" + p0.x + "¤" + p0.y + "¤" + p1.x + "¤" + p1.y + "¤"
				+ color.getRGB() + "¤" + fillPoly + "¤" + thickness;
	}

	@Override
	public void update(String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		setFirstPoint(Integer.parseInt(paramsList.get(1)), Integer
				.parseInt(paramsList.get(2)));
		setLastPoint(Integer.parseInt(paramsList.get(3)), Integer
				.parseInt(paramsList.get(4)));
		setColor(new Color(Integer.parseInt(paramsList.get(5))));
		setFillPoly(Boolean.parseBoolean(paramsList.get(6)));
		setThickness(Integer.parseInt(paramsList.get(7)));
	}

	/**
	 * @param id
	 * @param params
	 * @return Une nouvelle instance de figure à partir des paramètres
	 *         sauvegardés dans la chaîne params.
	 */
	public static Rect newBBRectangle(int id, String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		Point p0 = new Point(Integer.parseInt(paramsList.get(1)), Integer
				.parseInt(paramsList.get(2)));
		Point p1 = new Point(Integer.parseInt(paramsList.get(3)), Integer
				.parseInt(paramsList.get(4)));
		Color c = new Color(Integer.parseInt(paramsList.get(5)));
		boolean fill = Boolean.parseBoolean(paramsList.get(6));
		int thick = Integer.parseInt(paramsList.get(7));

		return new Rect(id, p0, p1, c, fill, thick);
	}
}
