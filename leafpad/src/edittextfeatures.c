/*
 *  Leafpad - GTK+ based simple text editor
 *  Copyright (C) 2004-2005 Tarot Osuji
 *  
 *  Feature added by Samy Badjoudj
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
#include "leafpad.h"
#include "string.h"

void
duplicate_line (GtkWidget *textview)
{
  GtkTextIter start, end;
  GtkTextBuffer *textbuffer = gtk_text_view_get_buffer (GTK_TEXT_VIEW (textview));
  GtkTextMark *cursor = gtk_text_buffer_get_insert (textbuffer);

  gtk_text_buffer_get_iter_at_mark (textbuffer, &start, cursor);
  gtk_text_buffer_get_iter_at_mark (textbuffer, &end, cursor);

  gtk_text_iter_set_line_offset (&start, 0);
  gtk_text_view_forward_display_line_end((GtkTextView *)textview,&end); 
  gchar *line = g_strconcat ("\n", gtk_text_buffer_get_text (textbuffer,
                                                             &start,
                                                             &end,
                                                             FALSE), NULL);
  g_signal_emit_by_name (G_OBJECT (textbuffer),
                         "begin-user-action");
  gtk_text_buffer_insert (textbuffer, &end, line,strlen(line));
  g_signal_emit_by_name (G_OBJECT (textbuffer),
                         "end-user-action");
  gtk_text_buffer_place_cursor (textbuffer, &end);
  g_free(line); 


}
