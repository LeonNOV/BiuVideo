package com.leon.biuvideo.beans.play;

import java.io.Serializable;
import java.util.List;

public class Play implements Serializable {
    public List<String> accept_description;
    public List<Media> videos;
    public List<Media> audios;
}