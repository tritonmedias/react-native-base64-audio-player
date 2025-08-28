import { NativeModules, NativeEventEmitter } from "react-native";
const { Base64Audio } = NativeModules;
const emitter = Base64Audio ? new NativeEventEmitter(Base64Audio) : null;
const API = {
    play: async (base64, mimeType = "audio/mpeg") => {
        if (!Base64Audio)
            throw new Error("Native module Base64Audio not found");
        return Base64Audio.play(base64, mimeType);
    },
    pause: async () => Base64Audio?.pause(),
    resume: async () => Base64Audio?.resume(),
    stop: async () => Base64Audio?.stop(),
    setVolume: async (v) => Base64Audio?.setVolume(v),
    unload: async () => Base64Audio?.unload(),
    addListener: (cb) => {
        if (!emitter)
            return () => { };
        const sub = emitter.addListener("Base64AudioEvent", cb);
        return () => sub.remove();
    },
};
export default API;
