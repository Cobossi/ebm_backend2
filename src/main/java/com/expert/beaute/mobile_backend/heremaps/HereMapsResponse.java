package com.expert.beaute.mobile_backend.heremaps;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HereMapsResponse {
    private List<HereMapsItem> items = new ArrayList<>();
}
