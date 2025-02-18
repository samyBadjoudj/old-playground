/* 
 * File:   sam_tail_http.c
 * Author: samy badjoudj
 *
 * Program to tail http file (or https) resource 
 * with or without authentication,
 * when you have no help from the server :D 
 */
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <curl/curl.h>
#include "smt_http_config.h"
#include "smt_http_appender.h"

#define SMT_HTTP_SIGINT_MSG "Stopped cleanly\n" 
#define SMT_HTTP_USAGE_MSG  "Wrong usage : sam_tail_http /your/absolute_path/yourfile.properties\n"

    
void handle_signal() {
    printf(ANSI_COLOR_RED SMT_HTTP_SIGINT_MSG ANSI_COLOR_RESET "\n");
    exit(EXIT_FAILURE);
}

int main(int argc, char**argv) {
    signal(SIGINT, handle_signal);
    if(argc==1){
        printf(SMT_HTTP_USAGE_MSG);
        return EXIT_FAILURE;
    }
    smt_http_conf_t * tail_config = get_config_from_filename(argv[1]);
    tail_file(tail_config);
    return EXIT_SUCCESS;
}
