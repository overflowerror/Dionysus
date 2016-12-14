package net.persei.dionysus;

import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.sound.midi.MidiUnavailableException;

import net.persei.dionysus.commands.Command;
import net.persei.dionysus.commands.DelayCommand;
import net.persei.dionysus.commands.MultiCommand;
import net.persei.dionysus.commands.MusicCommand;
import net.persei.dionysus.commands.MusicCommandType;
import net.persei.dionysus.commands.MusicPlayCommand;
import net.persei.dionysus.commands.ProvokeCommand;
import net.persei.dionysus.commands.ResetContextCommand;
import net.persei.dionysus.commands.SetContextCommand;
import net.persei.dionysus.commands.SoundFXComanndType;
import net.persei.dionysus.commands.SoundFXCommand;
import net.persei.dionysus.commands.VideoCommand;
import net.persei.dionysus.commands.VideoCommandType;
import net.persei.dionysus.commands.VideoPlayCommand;
import net.persei.dionysus.events.Event;
import net.persei.dionysus.exceptions.LibrariesNotFoundException;
import net.persei.dionysus.managers.CommandManager;
import net.persei.dionysus.managers.Context;
import net.persei.dionysus.managers.MidiManager;
import net.persei.dionysus.managers.MidiSource;
import net.persei.dionysus.managers.MusicManager;
import net.persei.dionysus.managers.PlayerManager;
import net.persei.dionysus.managers.Sequence;
import net.persei.dionysus.managers.SoundFXManager;
import net.persei.dionysus.managers.VideoManager;
import net.persei.dionysus.players.PlayerType;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Main {

	public static final String TITLE = "Dionysus";
	private static VideoManager videoManager;
	private static CommandManager commandManager;
	private static MidiManager midiManager;
	private static Sequence contextSequence;
	private static Context musicContext;
	private static Context fxContext;
	private static Context videoContext;
	private static Context sceneContext;
	private static MusicManager musicManager;
	private static Context fightContext;
	private static SoundFXManager soundManager;
	public static boolean test = false;
	private static Scanner scanner;

	public static void main(String[] args)
			throws LibrariesNotFoundException, MidiUnavailableException, InterruptedException, FileNotFoundException {
		if (!findLibs())
			throw new LibrariesNotFoundException();
		System.out.println(LibVlc.INSTANCE.libvlc_get_version());

		System.out.println("Instancing video manager...");
		videoManager = new VideoManager("MainVideoManager");
		PlayerManager.getInstance().create(PlayerType.Video, "primary", true);
		PlayerManager.getInstance().create(PlayerType.Video, "secondary", true);

		System.out.println("Instancing music manager...");
		musicManager = new MusicManager("MainMusicManager");

		System.out.println("Instancing sound fx manager...");
		soundManager = new SoundFXManager("MainSoundFXManager");

		System.out.println("Instancing command manager...");
		commandManager = new CommandManager();
		System.out.println("Instancing midi manager...");
		midiManager = new MidiManager(commandManager);

		System.out.println("Adding commands...");

		contextSequence = new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 7, 0));
		Sequence resetContext = new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 6, 0));

		musicContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 0, 0));
		fxContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 1, 0));
		videoContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 2, 0));
		sceneContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 3, 0));
		fightContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 4, 0));

		commandManager.addCommand(resetContext, new ResetContextCommand("ResetContext", commandManager));

		commandManager.addCommand(new Sequence(contextSequence).append(musicContext),
				new SetContextCommand("MusicContext", commandManager, musicContext));
		commandManager.addCommand(new Sequence(contextSequence).append(fxContext),
				new SetContextCommand("FXContext", commandManager, fxContext));
		commandManager.addCommand(new Sequence(contextSequence).append(videoContext),
				new SetContextCommand("VideoContext", commandManager, videoContext));
		commandManager.addCommand(new Sequence(contextSequence).append(sceneContext),
				new SetContextCommand("SceneContext", commandManager, sceneContext));
		commandManager.addCommand(new Sequence(contextSequence).append(fightContext),
				new SetContextCommand("FightContext", commandManager, fightContext));

		commandManager.addCommand(new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 5, 0)),
				new MultiCommand("kill-everybody",
						new MusicCommand("stop-music", musicManager, MusicCommandType.stop, null, 0, false),
						new SoundFXCommand("stop-sound", soundManager, SoundFXComanndType.stop, null),
						new VideoCommand("stop-video-primary", VideoCommandType.stop, videoManager, "primary", null,
								false),
						new VideoCommand("stop-video-secondary", VideoCommandType.stop, videoManager, "secondary", null,
								false)));

		// Real Commands
		/*
		 * ******************************************** NATURE
		 * ********************************************
		 */

		Context natureContext = new Context(sceneContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 0, 0));
		commandManager.addCommand(new Sequence(contextSequence).append(natureContext),
				new SetContextCommand("NutureSceneContext", commandManager, natureContext));

		/*
		 * FOREST
		 */

		Sequence forestNoWaterSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 1, 0));
		commandManager.addCommand(forestNoWaterSequence, basicScene("forest", "forest.mp4", "environment/peace.mp3"));

		Sequence forestWaterSequenceNoRain = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 1, 0));
		commandManager.addCommand(forestWaterSequenceNoRain,
				basicScene("forestWater2", "water2.mp4", "environment/peace2.mp3"));

		Sequence forestWaterSequenceNoRain2 = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 1, 0));
		commandManager.addCommand(forestWaterSequenceNoRain2,
				basicScene("forestWater", "water.mp4", "environment/quiet.mp3"));

		Sequence forestWaterSequenceRain = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 3, 1, 0));
		commandManager.addCommand(forestWaterSequenceRain,
				basicScene("forestRain", "lake-storm.mp4", "environment/scene-specific/elves-woods.mp3"));

		Sequence forestNoWaterChaseSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 1, 0));
		commandManager.addCommand(forestNoWaterChaseSequence,
				basicScene("forestChase", "forest.mp4", "environment/chase.mp3"));

		Sequence forestNoWaterChase2Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 1, 0));
		commandManager.addCommand(forestNoWaterChase2Sequence,
				basicScene("forestChase2", "forest.mp4", "environment/scene-specific/running-woods.mp3"));

		Sequence forestWater2Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 1, 0));
		commandManager.addCommand(forestWater2Sequence,
				basicScene("forestWater2", "river.mp4", "environment/scene-specific/river.mp3"));

		Sequence forestWater3Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 1, 0));
		commandManager.addCommand(forestWater3Sequence,
				basicScene("forestWater3", "forest2.mp4", "environment/beginning.mp3"));

		// TODO forest night

		Sequence forestNightSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 2, 0));
		commandManager.addCommand(forestNightSequence,
				basicScene("forestNight1", "forest-night.mp4", "environment/night.mp3"));

		Sequence forestNight2Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 2, 0));
		commandManager.addCommand(forestNight2Sequence,
				basicScene("forestNight2", "forest-night.mp4", "environment/mystic.mp3"));

		Sequence forestNight3Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 2, 0));
		commandManager.addCommand(forestNight3Sequence,
				basicScene("forestNight3", "forest-night.mp4", "environment/something-walk.mp3"));

		/*
		 * FIELD
		 */

		Sequence fieldSunSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 3, 0));
		commandManager.addCommand(fieldSunSequence, basicScene("field", "field.mp4", "environment/land.mp3"));

		Sequence fieldNoSunSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 3, 0));
		commandManager.addCommand(fieldNoSunSequence, basicScene("fieldNoSun", "field2.mp4", "environment/joyful.mp3"));

		Sequence fieldSun2Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 3, 0));
		commandManager.addCommand(fieldSun2Sequence, basicScene("field2", "field.mp4", "environment/life.mp3"));

		Sequence fieldNoSun3Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 3, 3, 0));
		commandManager.addCommand(fieldNoSun3Sequence,
				basicScene("fieldNoSun3", "field2.mp4", "environment/evening.ogg"));

		Sequence fieldSun3Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 3, 0));
		commandManager.addCommand(fieldSun3Sequence, basicScene("fieldNoSun", "field.mp4", "environment/journey.mp3"));

		/*
		 * MOUNTAIN
		 */

		Sequence mountainSeaSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 4, 0));
		commandManager.addCommand(mountainSeaSequence,
				basicScene("mountains", "mountains.mp4", "environment/equinox.mp3"));

		Sequence mountainSnowSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 4, 0));
		commandManager.addCommand(mountainSnowSequence, basicScene("snow", "snow-storm.mp4", "environment/now.mp3"));

		Sequence mountainSea2Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 4, 0));
		commandManager.addCommand(mountainSea2Sequence,
				basicScene("mountains", "mountains.mp4", "environment/glory.mp3"));

		Sequence mountainSnow2Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 3, 4, 0));
		commandManager.addCommand(mountainSnow2Sequence,
				basicScene("snow", "snow-storm.mp4", "environment/dynamic.mp3"));

		/*
		 * DESERT
		 */

		Sequence dessertSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 5, 0));
		commandManager.addCommand(dessertSequence,
				basicScene("dessert", "dessert3.mp4", "environment/scene-specific/dessert.mp3"));

		Sequence dessert2Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 5, 0));
		commandManager.addCommand(dessert2Sequence,
				basicScene("dessert2", "dessert.mp4", "environment/wind-journey.mp3"));

		Sequence dessert3Sequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 5, 0));
		commandManager.addCommand(dessert3Sequence, basicScene("dessert3", "dessert2.mp4", "environment/try.mp3"));

		/*
		 * OCEAN
		 */

		Sequence oceanSequence = new Sequence(natureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 6, 0));
		commandManager.addCommand(oceanSequence, basicScene("ocean", "ocean2.mp4", "environment/ocean.mp3"));

		Sequence ocean2Sequence = new Sequence(natureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 6, 0));
		commandManager.addCommand(ocean2Sequence, basicScene("ocean2", "ocean.mp4", "environment/quiet.mp3"));

		/*
		 * NIGHT
		 */

		Sequence nightSequence = new Sequence(natureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 7, 0));
		commandManager.addCommand(nightSequence, basicScene("night", "night.mp4", "environment/night.mp3"));

		Sequence night2Sequence = new Sequence(natureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 7, 0));
		commandManager.addCommand(night2Sequence, basicScene("night2", "night.mp4", "environment/mystic2.mp3"));

		Sequence nightmareSequence = new Sequence(natureContext)
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 7, 0));
		commandManager.addCommand(nightmareSequence, basicScene("nightmare", "night.mp4", "environment/nightmare.mp3"));

		/*
		 * GARDEN
		 */
		Sequence gardenSequence = new Sequence(natureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 7, 0));
		commandManager.addCommand(gardenSequence, basicScene("garden", "garden.mp4", "environment/friendly.mp3"));

		/*
		 * ******************************************** CITIES
		 * ********************************************
		 */

		Context cityContext = new Context(sceneContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 0, 0));
		commandManager.addCommand(new Sequence(contextSequence).append(cityContext),
				new SetContextCommand("CitySceneContext", commandManager, cityContext));

		Sequence elmaratu = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 1, 0));
		commandManager.addCommand(elmaratu,
				basicScene("elmaratu", "images/city/elmaratu.jpg", "environment/city-specific/elmaratu.mp3"));

		Sequence silvas = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 1, 0));
		commandManager.addCommand(silvas,
				basicScene("silvas", "images/city/silvas.jpg", "environment/city-specific/silvas.mp3"));

		Sequence feras = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 1, 0));
		commandManager.addCommand(feras,
				basicScene("feras", "images/city/feras.jpg", "environment/city-specific/feras.mp3"));

		Sequence finas = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 3, 2, 0));
		commandManager.addCommand(finas,
				basicScene("finas", "images/city/finas.jpg", "environment/city-specific/finas.ogg"));

		Sequence novis = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 2, 0));
		commandManager.addCommand(novis,
				basicScene("novis", "images/city/novis.jpg", "environment/city-specific/novis.mp3"));

		Sequence arata = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 3, 0));
		commandManager.addCommand(arata,
				basicScene("arata", "images/city/arata.jpg", "environment/city-specific/arata.ogg"));

		Sequence mariat = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 3, 0));
		commandManager.addCommand(mariat,
				basicScene("mariat", "images/city/mariat.jpg", "environment/city-specific/mariat2.mp3"));

		Sequence urbas = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 4, 0));
		commandManager.addCommand(urbas,
				basicScene("urbas", "images/city/urbas.jpg", "environment/city-specific/urbas.mp3"));

		Sequence flumas = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 5, 0));
		commandManager.addCommand(flumas,
				basicScene("flumas", "images/city/flumas.jpg", "environment/city-specific/flumas.ogg"));

		Sequence litera = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 5, 0));
		commandManager.addCommand(litera,
				basicScene("litera", "images/city/litera.jpg", "environment/city-specific/litera.ogg"));

		Sequence versura = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 3, 6, 0));
		commandManager.addCommand(versura,
				basicScene("versura", "images/city/versura.jpg", "environment/city-specific/versura.mp3"));

		Sequence arem = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 3, 0));
		commandManager.addCommand(arem,
				basicScene("arem", "images/city/arem2.jpg", "environment/city-specific/arem.mp3"));

		Sequence pesa = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 4, 0));
		commandManager.addCommand(pesa,
				basicScene("pesa", "images/city/pesa.jpg", "environment/city-specific/pesa.ogg"));

		Sequence mansas = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 5, 0));
		commandManager.addCommand(mansas,
				basicScene("mansas", "images/city/mansas.jpg", "environment/city-specific/mansas2.ogg"));

		Sequence lamata = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 4, 0));
		commandManager.addCommand(lamata,
				basicScene("lamata", "images/city/lamata.jpg", "environment/city-specific/lamata.mp3"));

		Sequence porta = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 6, 0));
		commandManager.addCommand(porta,
				basicScene("porta", "images/city/porta.jpg", "environment/city-specific/porta.mp3"));

		Sequence inlua = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 7, 0));
		commandManager.addCommand(inlua,
				basicScene("inlua", "images/city/inlua.jpg", "environment/city-specific/inlua.mp3"));

		Sequence medala = new Sequence(cityContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 7, 0));
		commandManager.addCommand(medala,
				basicScene("medala", "images/city/medala.jpg", "environment/city-specific/medala.mp3"));

		Context cityContext2 = new Context(sceneContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 0, 0));
		commandManager.addCommand(new Sequence(contextSequence).append(cityContext2),
				new SetContextCommand("CitySceneContext2", commandManager, cityContext2));

		Sequence mansas2 = new Sequence(cityContext2).append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 5, 0));
		commandManager.addCommand(mansas2,
				basicScene("mansas2", "images/city/mansas.jpg", "environment/city-specific/mansas.ogg"));

		Sequence urbas2 = new Sequence(cityContext2).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 4, 0));
		commandManager.addCommand(urbas2,
				basicScene("urbas2", "images/city/urbas.jpg", "environment/city-specific/urbas2.ogg"));

		Sequence flumas2 = new Sequence(cityContext2).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 5, 0));
		commandManager.addCommand(flumas2,
				basicScene("flumas2", "images/city/flumas.jpg", "environment/city-specific/flumas2.mp3"));

		Sequence elmaratu2 = new Sequence(cityContext2).append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 1, 0));
		commandManager.addCommand(elmaratu2,
				basicScene("elmaratu2", "images/city/elmaratu.jpg", "environment/city-specific/elmaratu3.mp3"));

		/*
		 * CULTURE
		 */

		Context cultureContext = new Context(sceneContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 0, 0));
		commandManager.addCommand(new Sequence(contextSequence).append(cultureContext),
				new SetContextCommand("CultureSceneContext", commandManager, cultureContext));

		Sequence church = new Sequence(cultureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 1, 0));
		commandManager.addCommand(church,
				new MultiCommand("church",
						new SoundFXCommand("playChurchDoor", soundManager, SoundFXComanndType.play,
								"resources/audio/sfx/door4.wav"),
						new DelayCommand("delayedChurchSounds",
								new SoundFXCommand("playChurchSounds", soundManager, SoundFXComanndType.play,
										"resources/audio/sfx/church2.wav"),
								500),
						basicScene("churchScene", "images/church.jpg", "gaudete.mp3")));

		Sequence church2 = new Sequence(cultureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 2, 0));
		commandManager.addCommand(church2,
				new MultiCommand("church2",
						new SoundFXCommand("playChurchDoor", soundManager, SoundFXComanndType.play,
								"resources/audio/sfx/door4.wav"),
						new DelayCommand("delayedChurch2Sounds",
								new SoundFXCommand("playChurch2Sounds", soundManager, SoundFXComanndType.play,
										"resources/audio/sfx/church2.wav"),
								500),
						basicScene("church2Scene", "images/church2.jpg", "shchedryk.mp3")));

		Sequence castle = new Sequence(cultureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 1, 0));
		commandManager.addCommand(castle, basicScene("castleScene", "images/castle.jpg", "environment/castle.mp3"));

		Sequence castle2 = new Sequence(cultureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 2, 0));
		commandManager.addCommand(castle2, basicScene("castleScene", "images/castle.jpg", "environment/king-.ogg"));

		Sequence heaven = new Sequence(cultureContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 7, 0));
		commandManager.addCommand(heaven, basicScene("heavenScene", "images/heaven.jpg", "angels.mp3"));

		/*
		 * FIREPLACE
		 */
		Context firePlaceContext = new Context(sceneContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 0, 0));
		commandManager.addCommand(new Sequence(contextSequence).append(firePlaceContext),
				new SetContextCommand("FireplaceSceneContext", commandManager, firePlaceContext));
		// TODO

		// TODO

		/*
		 * FIGHTS
		 */

		Sequence before = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 0, 0));
		commandManager.addCommand(before, new MusicPlayCommand("beforeMusic", musicManager,
				"resources/audio/environment/fights/before-fight.mp3", 1000, true));

		Sequence roundBasedIntro = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 0, 0));
		commandManager.addCommand(roundBasedIntro,
				new MultiCommand("roundBasedSplitter",
						new MusicCommand("stopMusic", musicManager, MusicCommandType.stop, null, 200, false),
						new SoundFXCommand("playDrop", soundManager, SoundFXComanndType.play,
								"resources/audio/sfx/subfrequ2.wav")));

		Sequence roundBased1 = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 1, 0));
		commandManager.addCommand(roundBased1, new MusicPlayCommand("round-based", musicManager,
				"resources/audio/environment/fights/fight-round-based.mp3", 200));

		Sequence facing = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 0, 0));
		commandManager.addCommand(facing,
				new MultiCommand("facing1",
						new MusicPlayCommand("facingMusic", musicManager,
								"resources/audio/environment/fights/alive.mp3", 200, false),
						new DelayCommand("facing2Delay",
								new ProvokeCommand("facing2Command",
										new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 1, 0)),
										commandManager),
								(2 * 60) * 1000)));

		Sequence black = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 1, 0));
		commandManager.addCommand(black, new MusicPlayCommand("blackMusic", musicManager,
				"resources/audio/environment/fights/black.mp3", 1000, true));

		Sequence confrontation = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 0, 0));
		commandManager.addCommand(confrontation, new MusicPlayCommand("confrontationMusic", musicManager,
				"resources/audio/environment/fights/confrontation.mp3", 1000, true));

		Sequence sorrow = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 7, 0));
		commandManager.addCommand(sorrow, new MusicPlayCommand("sorrowMusic", musicManager,
				"resources/audio/environment/fights/death-sorrow.ogg", 1000, true));

		Sequence devil = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 7, 0));
		commandManager.addCommand(devil, new MusicPlayCommand("devilMusic", musicManager,
				"resources/audio/environment/fights/devil.mp3", 1000, true));

		Sequence empire = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 7, 0));
		commandManager.addCommand(empire, new MusicPlayCommand("empireMusic", musicManager,
				"resources/audio/environment/fights/empire.mp3", 1000, true));

		Sequence empire2 = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 6, 0));
		commandManager.addCommand(empire2, new MusicPlayCommand("empire2Music", musicManager,
				"resources/audio/environment/fights/empire2.mp3", 1000, true));

		Sequence first = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 2, 0));
		commandManager.addCommand(first, new MusicPlayCommand("firstMusic", musicManager,
				"resources/audio/environment/fights/first.mp3", 1000, true));

		Sequence fleeing = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 5, 0));
		commandManager.addCommand(fleeing, new MusicPlayCommand("fleeingMusic", musicManager,
				"resources/audio/environment/fights/fleeing.mp3", 1000, true));

		Sequence fun = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 7, 0));
		commandManager.addCommand(fun, new MusicPlayCommand("funMusic", musicManager,
				"resources/audio/environment/fights/fun.ogg", 1000, true));

		Sequence gate = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 1, 0));
		commandManager.addCommand(gate, new MusicPlayCommand("gateMusic", musicManager,
				"resources/audio/environment/fights/gate.mp3", 1000, true));

		Sequence hell = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 4, 6, 0));
		commandManager.addCommand(hell, new MusicPlayCommand("hellMusic", musicManager,
				"resources/audio/environment/fights/hell.mp3", 1000, true));

		Sequence mountains = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 7, 0));
		commandManager.addCommand(mountains, new MusicPlayCommand("mountainsMusic", musicManager,
				"resources/audio/environment/fights/mountains-battle.ogg", 1000, true));

		Sequence power = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 0, 0));
		commandManager.addCommand(power, new MusicPlayCommand("powerMusic", musicManager,
				"resources/audio/environment/fights/power.mp3", 1000, true));

		Sequence redemption = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 7, 0));
		commandManager.addCommand(redemption, new MusicPlayCommand("redemptionMusic", musicManager,
				"resources/audio/environment/fights/redemption.mp3", 1000, true));

		Sequence secondary = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 3, 0));
		commandManager.addCommand(secondary, new MusicPlayCommand("secondaryMusic", musicManager,
				"resources/audio/environment/fights/secondary.mp3", 1000, true));

		Sequence slow = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 0, 0));
		commandManager.addCommand(slow, new MusicPlayCommand("slowMusic", musicManager,
				"resources/audio/environment/fights/slow.ogg", 1000, true));

		Sequence steel = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 2, 0));
		commandManager.addCommand(steel, new MusicPlayCommand("steelMusic", musicManager,
				"resources/audio/environment/fights/steel.mp3", 1000, true));

		Sequence time = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 4, 0));
		commandManager.addCommand(time, new MusicPlayCommand("timeMusic", musicManager,
				"resources/audio/environment/fights/time.mp3", 1000, true));

		Sequence trap = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 4, 0));
		commandManager.addCommand(trap, new MusicPlayCommand("trapMusic", musicManager,
				"resources/audio/environment/fights/trap.mp3", 1000, true));

		Sequence veniVediVeci = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 2, 0));
		commandManager.addCommand(veniVediVeci, new MusicPlayCommand("veniVediVeciMusic", musicManager,
				"resources/audio/environment/fights/veni-vedi-veci.mp3", 1000, true));

		Sequence victory = new Sequence(fightContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 6, 7, 0));
		commandManager.addCommand(victory, new MusicPlayCommand("victoryusic", musicManager,
				"resources/audio/environment/fights/victory.mp3", 1000, true));

		/*
		 * MUSIC
		 */

		Sequence army = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 0, 0));
		commandManager.addCommand(army,
				new MusicPlayCommand("armyMusic", musicManager, "resources/audio/environment/army.ogg", 1000, true));

		Sequence badTruth = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 2, 0));
		commandManager.addCommand(badTruth, new MusicPlayCommand("badTruthMusic", musicManager,
				"resources/audio/environment/bad-truth.mp3", 1000, true));

		Sequence blood = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 0, 0));
		commandManager.addCommand(blood,
				new MusicPlayCommand("bloodMusic", musicManager, "resources/audio/environment/blood.mp3", 1000, true));

		Sequence chance = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 0, 0));
		commandManager.addCommand(chance, new MusicPlayCommand("chanceMusic", musicManager,
				"resources/audio/environment/chance.mp3", 1000, true));

		Sequence change = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 2, 0));
		commandManager.addCommand(change, new MusicPlayCommand("changeMusic", musicManager,
				"resources/audio/environment/change.mp3", 1000, true));

		Sequence chase = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 3, 0));
		commandManager.addCommand(chase,
				new MusicPlayCommand("chaseMusic", musicManager, "resources/audio/environment/chase.mp3", 1000, true));

		Sequence danger = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 1, 0));
		commandManager.addCommand(danger, new MusicPlayCommand("dangerMusic", musicManager,
				"resources/audio/environment/danger.ogg", 1000, true));

		Sequence druidic = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 5, 0));
		commandManager.addCommand(druidic, new MusicPlayCommand("druidicMusic", musicManager,
				"resources/audio/environment/druidic.mp3", 1000, true));

		Sequence dwarfs = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 7, 0));
		commandManager.addCommand(dwarfs, new MusicPlayCommand("dwarfsMusic", musicManager,
				"resources/audio/environment/dwarfs.mp3", 1000, true));

		Sequence dwarfs2 = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 5, 6, 0));
		commandManager.addCommand(dwarfs2, new MusicPlayCommand("dwarfs2Music", musicManager,
				"resources/audio/environment/dwarfs2.mp3", 1000, true));

		Sequence lightDark = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 7, 0));
		commandManager.addCommand(lightDark, new MusicPlayCommand("lightDarkMusic", musicManager,
				"resources/audio/environment/light-dark.mp3", 1000, true));

		Sequence race = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 3, 5, 0));
		commandManager.addCommand(race,
				new MusicPlayCommand("raceMusic", musicManager, "resources/audio/environment/race.mp3", 1000, true));

		Sequence woundedLand = new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 7, 7, 0));
		commandManager.addCommand(woundedLand, new MusicPlayCommand("woundedLandMusic", musicManager,
				"resources/audio/environment/wounded-land.ogg", 1000, true));

		System.out.println("Adding shutdown hook...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				midiManager.close();
				Logger.close();
				scanner.close();
			}
		});


		scanner = new Scanner(System.in);
		
		while (true) {
			if (scanner.nextLine().contains("t")) {
				if (test) {
					System.out.println("Disabling test mode.");
					test = false;
				} else {
					System.out.println("Enabling test mode.");
					test = true;
				}
			}
		}
		
//		while (true)
//			Thread.sleep(Long.MAX_VALUE);
	}

	private static boolean findLibs() {
		return new NativeDiscovery().discover();
	}

	private static Command basicScene(String scene, String videoFile, String musicFile) throws FileNotFoundException {
		Command video = new VideoPlayCommand("show " + scene, videoManager, "primary", "resources/video/" + videoFile,
				true);
		Command audio = new MusicPlayCommand("play " + scene, musicManager, "resources/audio/" + musicFile, 1000, true);
		return new MultiCommand("scene " + scene, video, audio);
	}
}
