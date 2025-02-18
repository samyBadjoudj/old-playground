/*    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

#include <json-glib/json-glib.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include "json-prettifier.h"


const static gchar *RED = "\x1B[31m";
const static gchar *YEL = "\x1B[33m";
const static gchar *BLU = "\x1B[34m";
const static gchar *CYN = "\x1B[36m";
const static gchar *WHT = "\x1B[37m";
const static gchar *PINK = "\x1B[95m";
const static gchar *NO_COLOR = "\x1B[0m";
extern int errno ;

void display_version (){
    g_print("%s v. %d.%d\n", JSON_PRETTY_NAME, JSON_PRETTY_VERSION_MAJOR,JSON_PRETTY_VERSION_MINOR);
}

void display_usage (){
    g_print("Put json as stdin : \n cat json_file.json | json-prettifier.c \n or cat json_file.json | json-prettifier | less -R");

}

int main(int argc, char *argv[]) {

    const gchar * BRACKET_COLOR = RED;
    const gchar * SQUARE_BRACKET_COLOR = CYN;
    const gchar * STRING_KEY_COLOR = PINK;
    const gchar * STRING_VALUE_COLOR = YEL;
    const gchar * NUMBER_COLOR = BLU;
    const gchar * COLON_COMMA_COLOR = WHT;

    char *line = NULL;
    size_t bufsize = 64;
    GString *json_text = g_string_new("");

    int option = 0;
    while ((option = getopt(argc, argv,"vh?")) != -1) {
        switch (option) {
            case 'v' :
                display_version();
                break;
            case 'h' :
                display_usage();
            case '?' :
                display_usage();
                break;
            default:;
        }
        exit(0);
    }



      while (getline(&line, &bufsize, stdin) !=  EOF ) {
          g_string_append(json_text,line);
      }
    JsonParser *jsonParser = json_parser_new();
    GError *error = NULL;
    gboolean is_parsing_ok = json_parser_load_from_data(jsonParser, json_text->str, json_text->len, &error);
    if(!is_parsing_ok){
        g_print("could not parse json : error code %d\n",error->code);
        g_free(line);
        g_object_unref(jsonParser);
        g_string_free(json_text,true);
        exit(0);
    }
    JsonNode *root = json_parser_get_root(jsonParser);
    JsonGenerator *gen = json_generator_new();
    json_generator_set_root(gen, root);
    json_generator_set_pretty(gen, true);
    gchar *raw_result = json_generator_to_data(gen, NULL);
    gboolean is_string_running = false;

    g_object_unref(jsonParser);
    g_object_unref(gen);
    json_node_free(root);

    const gchar *current_color;
    long len = (long) strlen(raw_result);
    int i = 0;
    for (i = 0; i < len; i++) {
        gchar current_char = raw_result[i];
        bool is_string_flag = current_char == '"' ;
        bool previous_is_key = raw_result[i - 2] == ':';


        if (!is_string_running) {
            switch (current_char) {
                case ',':
                case ':':
                        current_color = COLON_COMMA_COLOR;
                    break;
                case '{':
                case '}':
                        current_color = BRACKET_COLOR;
                    break;
                case '[':
                case ']':
                        current_color = SQUARE_BRACKET_COLOR;
                    break;
                case '.' :
                case '0' :
                case '1' :
                case '2' :
                case '3' :
                case '4' :
                case '5' :
                case '6' :
                case '7' :
                case '8' :
                case '9' :
                        current_color = NUMBER_COLOR;
                    break;
                default:;
            }
        }
        if (is_string_flag){
            current_color = previous_is_key || !strcmp(current_color, STRING_VALUE_COLOR) ? STRING_VALUE_COLOR  : STRING_KEY_COLOR;
            is_string_running = !is_string_running;
        }

        g_print("%s%c", current_color, current_char);
    }
    g_print("%s\n", NO_COLOR);
    g_free(raw_result);
    g_free(line);
    g_string_free(json_text,true);
    exit(0);
}
