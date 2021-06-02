package sLang.tokenizer;

import sLang.exceptions.SLangException;

public interface Tokenizer {
    abstract Token tokenize(String input, int index) throws SLangException;
}
