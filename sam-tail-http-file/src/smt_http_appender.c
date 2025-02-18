/* 
 * File:   sam_http_appender.c
 * Author: samy badjoudj
 * 
 *
 */
#define _GNU_SOURCE
#include <stdlib.h>
#include <stdio.h>

#include <string.h>
#include <curl/curl.h>
#include <unistd.h>
#include "smt_http_config.h"
#include "smt_http_appender.h"
#include <signal.h>


#define _GNU_SOURCE
#define ANSI_COLOR_RED      "\x1b[31m"
#define ANSI_COLOR_RESET    "\x1b[0m"


static smt_http_running = 1;
static long number_lines_new = 0L;

size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream) {
    int written = fwrite(ptr, size, nmemb, (FILE *) stream);
    return written;
}



CURL * get_curl(smt_http_conf_t * const tail_config) {

    curl_global_init(CURL_GLOBAL_ALL);
    CURL * curl;
    curl = curl_easy_init();

    if (curl) {
        curl_easy_setopt(curl, CURLOPT_NOPROGRESS, 1L);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, tail_config->content_file);
        curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
        curl_easy_setopt(curl, CURLOPT_USERNAME, tail_config->login);
        curl_easy_setopt(curl, CURLOPT_PASSWORD, tail_config->pass);
        curl_easy_setopt(curl, CURLOPT_URL, tail_config->url);
    }

    return curl;
}

void print_line(smt_http_conf_t * const tail_config, char * line) {
    char * color = NULL;
    int i;

    for (i = 0; i < tail_config->success_w->word_number; i++) {
        color = strcasestr(line, tail_config->success_w->word_to_match[i]) != NULL
                ? tail_config->success_w->color : color;
    }


    for (i = 0; i < tail_config->errors_w->word_number; i++) {
        color = strcasestr(line, tail_config->errors_w->word_to_match[i]) != NULL
                ? tail_config->errors_w->color : color;
    }

    if (color != NULL)
        printf("%s", color);

    printf("%s", line);

    if (color != NULL)
        printf(ANSI_COLOR_RESET);

}

int append(smt_http_conf_t * const tail_config) {

    char * line = NULL;
    size_t len = 0;
    ssize_t read = -1;
    long number_lines = 0L;

    while ((read = getline(&line, &len, tail_config->content_file)) != -1) {
        number_lines++;
        if (number_lines > number_lines_new) {
            number_lines_new++;
            print_line(tail_config, line);
        }
        len = 0;
    }

    if (line) {
        free(line);
    }

    return EXIT_SUCCESS;

}

int tail_file(smt_http_conf_t * const tail_config) {
    CURL * curl_main = get_curl(tail_config);
    while (smt_http_running) {
        
        tail_config->content_file = fopen(tail_config->content_file_name, "w");
        curl_easy_setopt(curl_main, CURLOPT_WRITEDATA, tail_config->content_file);
        curl_easy_perform(curl_main);
        fclose(tail_config->content_file);
        tail_config->content_file = fopen(tail_config->content_file_name, "r");
        append(tail_config);
        fclose(tail_config->content_file);
        sleep(tail_config->interval_secs);
    }

    //curl_easy_cleanup(curl_main);
    return EXIT_SUCCESS;
}


