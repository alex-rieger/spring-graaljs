package rieger.alex.springgraaljs.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public ResponseEntity<String> indexAction() {

        HttpHeaders httpHeaders = new HttpHeaders();
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("graal.js");

        // InputStream stencilHydrateInputStream = getClass().getResourceAsStream("/stencil-hydrate.js");
        InputStream stencilHydrateInputStream = getClass().getResourceAsStream("/render-to-string.js");

        Reader stencilHydrateScript = new InputStreamReader(stencilHydrateInputStream);

        String markup = "<my-component first=\"Alexander\" last=\"Rieger\">";

        Map<String, String> options = new HashMap<>();
        options.put("js.commonjs-require", "true");
        options.put("js.commonjs-require-cwd", "/");
        // options.put("js.nashorn-compat", "true");

        Context ctx = Context.newBuilder("js")
            .allowExperimentalOptions(true)
            .allowAllAccess(true)
            .options(options)
            .build();
        Source src;
        try {
            src = Source.newBuilder("js", stencilHydrateScript, "stencil.js").build();
            ctx.eval(src);
        } catch (IOException e) {
            e.printStackTrace();
        }

        

        // scriptEngine.eval(stencilHydrateScript);
        // scriptEngine.eval("const boo = await renderToString(" + markup + ");
        // console.log(boo.html)");

        // scriptEngine.eval("console.log('hello i am javascript')");
        return new ResponseEntity<>("hel", httpHeaders, HttpStatus.OK);

    }
}