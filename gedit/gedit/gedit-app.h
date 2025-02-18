/*
 * gedit-app.h
 * This file is part of gedit
 *
 * Copyright (C) 2005 - Paolo Maggi
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

#ifndef GEDIT_APP_H
#define GEDIT_APP_H

#include <gtk/gtk.h>
#include <gedit/gedit-window.h>

G_BEGIN_DECLS

#define GEDIT_TYPE_APP (gedit_app_get_type())

G_DECLARE_DERIVABLE_TYPE (GeditApp, gedit_app, GEDIT, APP, GtkApplication)

struct _GeditAppClass
{
	GtkApplicationClass parent_class;

	gboolean (*show_help)                   (GeditApp    *app,
	                                         GtkWindow   *parent,
	                                         const gchar *name,
	                                         const gchar *link_id);

	gchar *(*help_link_id)                  (GeditApp    *app,
	                                         const gchar *name,
	                                         const gchar *link_id);

	void (*set_window_title)                (GeditApp    *app,
	                                         GeditWindow *window,
	                                         const gchar *title);

	GeditWindow *(*create_window)           (GeditApp    *app);

	gboolean (*process_window_event)        (GeditApp    *app,
	                                         GeditWindow *window,
	                                         GdkEvent    *event);
};

GeditWindow	*gedit_app_create_window		(GeditApp    *app,
							 GdkScreen   *screen);

GList		*gedit_app_get_main_windows		(GeditApp    *app);

GList		*gedit_app_get_documents		(GeditApp    *app);

GList		*gedit_app_get_views			(GeditApp    *app);

gboolean	 gedit_app_show_help			(GeditApp    *app,
                                                         GtkWindow   *parent,
                                                         const gchar *name,
                                                         const gchar *link_id);

void		 gedit_app_set_window_title		(GeditApp    *app,
                                                         GeditWindow *window,
                                                         const gchar *title);
gboolean	gedit_app_process_window_event		(GeditApp    *app,
							 GeditWindow *window,
							 GdkEvent    *event);

G_END_DECLS

#endif /* GEDIT_APP_H */

/* ex:set ts=8 noet: */
