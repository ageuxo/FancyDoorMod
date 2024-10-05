package io.github.ageuxo.FancyDoorMod.block;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.FancyDoorMod.block.entity.DetectorBlockEntity;
import io.github.ageuxo.FancyDoorMod.network.NetRegistry;
import io.github.ageuxo.FancyDoorMod.network.packet.S2CDetectorPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DetectorBlock extends BaseEntityBlock {
    private static final Logger LOGGER = LogUtils.getLogger();

    public DetectorBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction facing = pContext.getNearestLookingDirection();
        boolean flip = false;
        if (pContext.getPlayer() != null) {
            flip = pContext.getPlayer().isCrouching();
        }
        return this.defaultBlockState().setValue(BlockStateProperties.FACING, flip ? facing : facing.getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DetectorBlockEntity(pPos, pState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand) == ItemStack.EMPTY) {
            if (!pLevel.isClientSide){
                if (pLevel.isLoaded(pPos)) {
                    if (pLevel.getBlockEntity(pPos) instanceof DetectorBlockEntity be && pPlayer instanceof ServerPlayer serverPlayer) {
                        LOGGER.debug("Sending packet...");
                        NetRegistry.INSTANCE.send(PacketDistributor.PLAYER.with(()-> serverPlayer),
                                new S2CDetectorPacket(
                                        pPos,
                                        be.getXValue(), be.getXMin(), be.getXMax(),
                                        be.getYValue(), be.getYMin(), be.getYMax(),
                                        be.getZValue(), be.getZMin(), be.getZMax()
                                )
                        );
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                LOGGER.debug("Requesting Detector Config Screen for {}", pPos);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
