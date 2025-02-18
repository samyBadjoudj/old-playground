/* 
 * File:   smt_http_config.c
 * Author: samy badjoudj
 * 
 *
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <confuse.h>
#include "smt_http_config.h"

#define SMT_HTTP_URL             "url"
#define SMT_HTTP_PASS            "pass"
#define SMT_HTTP_LOGIN           "login"
#define SMT_HTTP_FILE_NAME       "filename.path"
#define SMT_HTTP_INTERVAL        "interval"
#define SMT_HTTP_SUCCESS_WORDS   "words.success"
#define SMT_HTTP_ERROR_WORDS     "words.error"
#define SMT_PROP_FILE_MSG        "You need to fill the parameter \t-> "
#define SMT_DLT_INTERVAL         10
#define SMT_DLT_STRING           "\0"
#define SMT_DLT_FILE_NAME        "/tmp/no_name.out"


const char * DELIMITER = ";";

void fill_splitted_word(char *str, char * * splitted_word) {

    if (strlen(str) > 0) {
        char *token = NULL;
        int cnt = 0;

        token = strtok(str, DELIMITER);
        splitted_word[0] = malloc((strlen(token) + 1));
        strcpy(splitted_word[0], token);
        while (token != NULL) {

            token = strtok(NULL, DELIMITER);

            if (token != NULL) {
                cnt++;
                splitted_word[cnt] = malloc((strlen(token) + 1));
                strcpy(splitted_word[cnt], token);
            }
        }
    }
}

void is_initialization_ok(smt_http_conf_t * const tail_config) {

    if (strlen(tail_config->url) == 0) {
        printf(SMT_PROP_FILE_MSG SMT_HTTP_URL "\n");
        exit(EXIT_FAILURE);
    }
    
}

int get_number_of_chunk(char *line) {

    if (strlen(line) == 0) return 0;

    int i = 0;
    int cnt = 1;
    int l = strlen(line);

    for (i = 0; i < l; i++) {
        if (DELIMITER[0] == line[i]) {
            cnt++;
        }
    }

    return cnt;
}

smt_http_conf_t * get_config_from_filename(char * const filename) {

    smt_http_conf_t *tail_config = malloc(sizeof (smt_http_conf_t));
    char *err_w = NULL;
    char *suc_w = NULL;

    cfg_opt_t opts[] = {
        CFG_SIMPLE_STR(SMT_HTTP_URL, &tail_config->url),
        CFG_SIMPLE_STR(SMT_HTTP_PASS, &tail_config->pass),
        CFG_SIMPLE_STR(SMT_HTTP_LOGIN, &tail_config->login),
        CFG_SIMPLE_STR(SMT_HTTP_FILE_NAME, &tail_config->content_file_name),
        CFG_SIMPLE_INT(SMT_HTTP_INTERVAL, &tail_config->interval_secs),
        CFG_SIMPLE_STR(SMT_HTTP_SUCCESS_WORDS, &suc_w),
        CFG_SIMPLE_STR(SMT_HTTP_ERROR_WORDS, &err_w),
        CFG_END()
    };

    /*Default values in case of the properties files is not filled*/
    suc_w = strdup(SMT_DLT_STRING);
    err_w = strdup(SMT_DLT_STRING);
    tail_config->url = strdup(SMT_DLT_STRING);
    tail_config->pass = strdup(SMT_DLT_STRING);
    tail_config->content_file_name = strdup(SMT_DLT_FILE_NAME);
    tail_config->interval_secs = SMT_DLT_INTERVAL;

    cfg_t *cfg = cfg_init(opts, 0);
    cfg_parse(cfg, filename);
    cfg_free(cfg);

    is_initialization_ok(tail_config);


    int word_number = get_number_of_chunk(suc_w);
    tail_config->success_w = malloc(sizeof (smt_http_words_t));
    tail_config->success_w->word_to_match = malloc(word_number * sizeof (char *));
    tail_config->success_w->word_number = word_number;
    fill_splitted_word(suc_w, tail_config->success_w->word_to_match);
    tail_config->success_w->color = ANSI_COLOR_GREEN; /*Put it as property*/

    word_number = get_number_of_chunk(err_w);
    tail_config->errors_w = malloc(sizeof (smt_http_words_t));
    tail_config->errors_w->word_to_match = malloc(word_number * sizeof (char *));
    fill_splitted_word(err_w, tail_config->errors_w->word_to_match);
    tail_config->errors_w->word_number = word_number;
    tail_config->errors_w->color = ANSI_COLOR_RED; /*Put it as property*/

    return tail_config;

}

void clean_up_smt(smt_http_conf_t * config) {
    int i;

    for (i = 0; i < config->success_w->word_number; i++)
        free(config->success_w->word_to_match[i]);

    for (i = 0; i < config->errors_w->word_number; i++)
        free(config->errors_w->word_to_match[i]);

    free(config->errors_w->word_to_match);
    free(config->success_w->word_to_match);

    free(config->success_w);
    free(config->errors_w);
    free(config->content_file);
    free(config->login);
    free(config->pass);
    free(config->url);
    free(config->content_file_name);
    free(config);
}




