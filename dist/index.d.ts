declare const API: {
    play: (base64: string, mimeType?: string) => Promise<any>;
    pause: () => Promise<any>;
    resume: () => Promise<any>;
    stop: () => Promise<any>;
    setVolume: (v: number) => Promise<any>;
    unload: () => Promise<any>;
    addListener: (cb: (event: any) => void) => () => void;
};
export default API;
