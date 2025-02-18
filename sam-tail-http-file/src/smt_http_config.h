/* 
 * File:   smt_http_config.h
 * Author: samy badjoudj
 *
 * 
 */

#ifndef SAM_TAIL_CONFIG_H
#define	SAM_TAIL_CONFIG_H

#ifdef	__cplusplus
extern "C" {
#endif
    
#define ANSI_COLOR_RED     "\x1b[31m"
#define ANSI_COLOR_GREEN   "\x1b[32m"
#define ANSI_COLOR_YELLOW  "\x1b[33m"
#define ANSI_COLOR_BLUE    "\x1b[34m"
#define ANSI_COLOR_MAGENTA "\x1b[35m"
#define ANSI_COLOR_CYAN    "\x1b[36m"
#define ANSI_COLOR_RESET   "\x1b[0m"
    
    
typedef struct smt_http_words_t{
   char *color;
   size_t word_number;
   char **word_to_match;
}smt_http_words_t;


typedef struct smt_http_conf_t{
    int  interval_secs;
    char *login;
    char *pass;
    char *url;
    char *content_file_name;
    FILE *content_file;
    smt_http_words_t *success_w;
    smt_http_words_t *errors_w;
}smt_http_conf_t;


smt_http_conf_t * get_config_from_filename(char * const filname); 
void clean_up_smt(smt_http_conf_t * config);

#ifdef	__cplusplus
}
#endif
#endif	/* SAM_TAIL_CONFIG_H */


