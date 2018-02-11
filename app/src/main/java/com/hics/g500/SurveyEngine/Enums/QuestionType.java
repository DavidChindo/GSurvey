package com.hics.g500.SurveyEngine.Enums;

/**
 * Created by david.barrera on 2/5/18.
 */

public class QuestionType {

    //Parents
    public static final int OPEN_ENDED = 1;//Pa
    public static final int OPEN_ENDED_NUMBER = 2;
    public static final int OPEN_SEARCH = 3;
    public static final int GPS = 4;
    public static final int IMAGE = 5;
    public static final int MULTI_CHOICE_RADIO = 6;
    public static final int MULTI_CHOICE_CHECK = 7;
    public static final int MULTI_CHOICE_SPINNER = 8;
    public static final int SECTION_LABEL = 5;
    public static final int DISCLAIMER = 6;

    public static final int START_DATE = 12;
    public static final int END_DATE = 13;


    /*  TIPO DE PREGUNTAS
    1	Texto libre
2	Número
3	Campo de Búsqueda
4	Ubicación
5	Imagen
6	Radio Button
7	Checkbox
8	Lista desplegable
    */

    //Mask
    /*
    Masacaras para el tipo de Pregunta Abierta
     */
    public static final int MASK_OPEN_TEXT = 1;
    public static final int MASK_DATE = 2;
    public static final int MASK_DATE_TIME = 3;
    public static final int MASK_TIME = 4;
    public static final int MASK_PASSWORD = 5;
    public static final int MASK_BAR_CODE = 6;
    public static final int MASK_REPEAT = 7;

    /*
    Mascaras para el tipo de pregunta GPS
     */
    public static final int MASK_BOTTON = 1;
    public static final int MASK_MAP = 2;
    public static final int MASK_AUTOMATIC = 3;

    /*
    Mascaras para el tipo de pregunta Multimedia
     */
    public static final int MASK_PHOTO = 1;
    public static final int MASK_FILE = 2;
    public static final int MASK_AUDIO = 3;
    public static final int MASK_VIDEO = 4;

}
